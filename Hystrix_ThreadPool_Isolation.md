# Hystrix断路器的线程池隔离

## 什么是断路器？
在分布式服务调用中，为了提高稳定性，对调用进行监控，并提供：
- 动态降级
- 熔断
- 自动恢复
等保护功能的组件被称为断路器。

Hystrix断路器，是netflix开源的一个组件, 被集成在SpringCloud套件中。

要想深入了解Hystrix的实现原理，需要了解：
- Command模式
- 线程池
- Observable（RxJava）

简单理解就是：

Hystrix = Command模式 + ThreadPool + Observable(RxJava)

## 什么是线程池？


## 什么是Command模式？
Command模式是一个行为型模式，
它通过对调用行为本身进行封装来达到
对调用引起的依赖关系的一种解耦。

把对某服务的多次调用，转变成对多个command对象的处理。

## Hystrix如何实现线程池隔离？
HystrixCommand本身拥有一个线程池threadPool, 这个线程池是在其父类中由一个单例模式的工厂类
生成,以保证一个服务对应一个线程池。
```
abstract class AbstractCommand<R> implements HystrixInvokableInfo<R>, HystrixObservable<R> {
...
    private static HystrixThreadPool initThreadPool(HystrixThreadPool fromConstructor, HystrixThreadPoolKey threadPoolKey, HystrixThreadPoolProperties.Setter threadPoolPropertiesDefaults) {
            ...
            return HystrixThreadPool.Factory.getInstance(threadPoolKey, threadPoolPropertiesDefaults);
            ...
    }
```


同一服务，使用同一个线程池，不同的服务则使用不同的线程池，
包括两个层次的隔离：
- 不同服务，用不同的线程池隔离。
- 同一服务的不同方法，使用同一线程池的不同工作者线程隔离。

HystrixCommand继承AbstractCommand，而AbstractCommand就拥有一个线程池：
- protected final HystrixThreadPool threadPool;


## 如何得到结果？
```
    public Future<R> queue() {
        ...
        final Future<R> delegate = toObservable().toBlocking().toFuture();
        ...
    }
```    

```html

public abstract class HystrixCommand<R> extends AbstractCommand<R> implements HystrixExecutable<R>, HystrixInvokableInfo<R>, HystrixObservable<R> {
    ...
    @Override
    final protected Observable<R> getExecutionObservable() {
        return Observable.defer(new Func0<Observable<R>>() {
            @Override
            public Observable<R> call() {
                    ...
                    return Observable.just(run());
                    ...
            }
        })
        ...
    }
    ...
}

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
Command把调用委托给其他线程执行，而线程是异步调用，如何得到结果呢？

等！！！等他执行完。

## 如何调用降级方法？

## 如何自动恢复？


## 其他隔离方式
信号量隔离。

## 参考资料
https://my.oschina.net/7001/blog/1619842
