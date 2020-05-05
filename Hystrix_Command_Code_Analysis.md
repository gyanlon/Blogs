# 断路器Hystrix的实现机制
## 什么是断路器？
在分布式服务调用中，为了提高稳定性，通过对调用进行监控并提供动态降级
、熔断、自动恢复等保护功能的组件被称为断路器。

Hystrix断路器是netflix开源的一个组件, 被集成在SpringCloud套件中。

Hystrix提供了两种资源隔离方式：线程池、信号量。本文只探讨线程池相关的实现。

在讨论Hystrix的实现机制之前，您需要熟悉以下3个概念：
- Command模式
- [线程池隔离](https://blog.csdn.net/coobee/article/details/105841302)
- 响应式编程(RxJava Observable)
    - [简单理解Observable](https://blog.csdn.net/coobee/article/details/105817994)
    - [Observable.defer](https://blog.csdn.net/coobee/article/details/105817994) 

本文通过三个段落来探讨这个问题:
- [启动][1]
- [异步执行][2]
- [返回结果][3]

## 如何启动？
[1]: 1
Hystrix使用了Command模式来实现，Command模式是一个行为型模式，它通过对调用行为本身进行封装来达到对调用依赖关系的一种解耦。
HystrixCommand提供了注解的使用方式。而注解则通过Aspect来解释执行。
这部分代码在com.netflix.hystrix:hystrix-javanica库中。代码如下：
```html
/**
 * AspectJ aspect to process methods which annotated with {@link HystrixCommand} annotation.
 */
@Aspect
public class HystrixCommandAspect {
...
    @Around("hystrixCommandAnnotationPointcut() || hystrixCollapserAnnotationPointcut()")
    public Object methodsAnnotatedWithHystrixCommand(final ProceedingJoinPoint joinPoint) throws Throwable {
        ...
        HystrixInvokable invokable = HystrixCommandFactory.getInstance().create(metaHolder);
        ...
        result = CommandExecutor.execute(invokable, executionType, metaHolder);
        ...
        return result;
    }
...
}

```
解释一下比较重要的两处代码：
- HystrixCommandFactory.getInstance().create(metaHolder)： 

    根据annotation信息生成HystrixCommand对象
- CommandExecutor.execute(invokable, executionType, metaHolder); 

    执行HytrixCommand对象得到结果。Command内部会把真正的调用委托给其线程池执行，

## 如何异步执行?
[2]: 2
HystrixCommand还有一个线程池threadPool, 这个线程池是在其父类中由一个单例模式的工厂类
生成,以保证一个服务对应一个线程池,对应代码如下：
```
abstract class AbstractCommand<R> implements HystrixInvokableInfo<R>, HystrixObservable<R> {
    ...
    protected final HystrixThreadPool threadPool;
    ...
    private static HystrixThreadPool initThreadPool(HystrixThreadPool fromConstructor, HystrixThreadPoolKey threadPoolKey, HystrixThreadPoolProperties.Setter threadPoolPropertiesDefaults) {
            ...
            return HystrixThreadPool.Factory.getInstance(threadPoolKey, threadPoolPropertiesDefaults);
            ...
    }
```
其隔离的层次包括二层：
- 不同服务，用不同的线程池隔离。
- 同一服务的不同方法，使用同一线程池的不同工作者线程隔离。

Hystrix通过Rxjava的Observale对象的线程切换方式将具体的服务调用逻辑委托给线程池执行，源码如下：
```
abstract class AbstractCommand<R> implements HystrixInvokableInfo<R>, HystrixObservable<R> {
    protected final HystrixThreadPool threadPool;
    private Observable<R> executeCommandWithSpecifiedIsolation(final AbstractCommand<R> _cmd) {
            ...
            return Observable.defer(...)
                   .subscribeOn(threadPool.getScheduler(...));
    }
}    
```
其中
- subscribeOn(threadPool.getScheduler(...));

    这个操作符是将Observable对象的数据生产逻辑切换到线程池去执行。它后面会使用toBlocking()方法再将工作者线程
切换回主线程。

## 如何返回结果？
[3]: 3
HystrixCommand内部会委托线程池来异步执行，那么结果是如何返回的呢？
HystrixCommand.execute执行并返回接口，其代码如下：
```
public abstract class HystrixCommand<R> extends AbstractCommand<R> implements HystrixExecutable<R>, HystrixInvokableInfo<R>, HystrixObservable<R> {
    ...
    public R execute() {
        ...
        return queue().get();
        ...
    }
    public Future<R> queue() {
        ...
        final Future<R> delegate = toObservable().toBlocking().toFuture();
        ...
    }
    ...
```    
最主要的是这段代码：toObservable().toBlocking().toFuture()，它返回一个Future对象，通过Future对象的get方法来返回最终结果。具体解释一下：
- toObservable()：
 
    就是把Command的执行结果转变成被观察的数据方(Observable).
- toBlocking() 

    Converts an Observable into a BlockingObservable
- toFuture()

    订阅Observable对象，返回Future对象。

Observable对象就是一个数据生成器，它的数据来自具体的服务调用结果，源码如下：
```html

public abstract class HystrixCommand<R> extends AbstractCommand<R> implements HystrixExecutable<R>, HystrixInvokableInfo<R>, HystrixObservable<R> {
    ...
    @Override
    final protected Observable<R> getExecutionObservable() {
        return Observable.defer(new Func0<Observable<R>>() {
            @Override
            public Observable<R> call() {
                    ...
                    return Observable.just(run());  //生成数据的Observable对象
                    ...
            }
        })
        ...
    }
    ...
}
```

解释一下上述代码：
- run() 

    是Command的核心，它发起真实的rest调用，返回结果数据。
- Observable.just(run()) 

    将结果转变成Observable对象。
- Observable.defer

    延迟了结果数据的生成时间，它只会在这个Observable注册Observer的时候才调用。如果对Observable.defer有点费解，请参考：
[Observable.defer](https://blog.csdn.net/coobee/article/details/105817994)

下面是HystrixCommand中run()的代码，可以看出它的核心就是一个http调用。
```

public abstract class AbstractRibbonCommand<LBC extends AbstractLoadBalancerAwareClient<RQ, RS>, RQ extends ClientRequest, RS extends HttpResponse> extends HystrixCommand<ClientHttpResponse> implements RibbonCommand {
    ...
    protected ClientHttpResponse run() throws Exception {
        ...
        response = (HttpResponse)this.client.execute(request, this.config);
        ...
        return new RibbonHttpResponse(response);
    }
    ...
}
```

## 总结
Hystrix通过annotation将调用封装给Command，然后委托给线程池执行，再通过
Observable机制将异步结果返回。理解了核心实现机制，其他异常处理逻辑自然不难理解。

## 参考资料
https://my.oschina.net/7001/blog/1619842
https://www.jianshu.com/p/3e5d53e891db
https://www.jianshu.com/p/b037dbae9d8f
https://stackoverflow.com/questions/44658357/rxjava-scheduler-to-observe-on-main-thread