# Java线程池的数据传输

Java线程池ThreadPoolExecutor有两个重要的方法execute和submit.
解释如下：
- execute : 异步运行任务，没有返回值。
- submit : 异步运行任务，有返回值。

既然是异步运行，如何获取返回值呢？submit方法通过FutureTask对象返回任务返回值。


## ThreadPoolExecutor.submit

接口定义如下：
```html

public interface ExecutorService extends Executor {
    ...
    <T> Future<T> submit(Callable<T> var1);
    ...
}
```

序列图如下：

![Image](./images/executorservice_future_task.png
)

submit内部主要包含如下步骤：
 - 1, 将task对象封装成futuretask对象。
 - 2，生成工作者线程worker，并开始异步执行任务。
 - 3，返回futuretask对象。
 
FutureTask提供get方法来返回具体的任务执行结果，实现逻辑如下： 
## FutureTask内部实现
FutureTask实现了两个接口Runnable 和 Future, 说明它既是一个task，包含任务逻辑和参数，
又是一个future对象，用于返回异步结果。

```
public class FutureTask<V> implements RunnableFuture<V> {

    private Callable<V> callable;   // 任务
    private Object outcome;         // 任务执行结果
    private volatile Thread runner; //线程

    ...
    public FutureTask(Callable<V> callable) {
        ...
        this.callable = callable;
        this.state = 0;
        ...
    }
    ...
    public void run() {
        ...
        Callable c = this.callable;
        ...
        result = c.call();
        ...
        this.set(result);
    }
    
    public V get() throws InterruptedException, ExecutionException {
        ...
        this.awaitDone(false, 0L);
        ...
        return this.report(s);
        ...
    }    
}
```

从上述源码中可以看出：
- run方法被线程调用来执行任务，并将结果存入outcome。
- get方法会以阻塞的方式等待线程执行完毕，然后返回outcome中结果。

## 参考资料
https://blog.csdn.net/wei_lei/article/details/74262818
