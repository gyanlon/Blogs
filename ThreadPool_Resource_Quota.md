# 简单理解Java线程池的资源隔离

## 什么是线程池
线程池是一个线程管理器，它通过管理一组工作者线程来达到：
- 线程复用
- 资源限制

的目的。

## 线程的资源分配
线程自身拥有的资源很少，除了运行中必不可少的资源
（寄存器，栈，程序计数器等），其他所用到的大部分资源，都属于进程，
与同一个进程的其他线程共享。

## 线程池隔离
既然线程本身的资源很少，如何用线程池来隔离资源呢？
准确的说，线程池是通过限制线程数，来对资源使用进行**限制**，而不是隔离。
限制资源使用，防止系统过载。

## 线程池和资源的关系图
```
                   +----------+     +---------+      +------+
                   |  Process +-----+  Heap   +------+      |
                   | (Program)|     +----+----+      |  M   |
                   +----+-----+          |           |  e   |
                        |                |           |  m   |
+----------+       +----+-----+          |           |  o   |
|ThreadPool+-------+Thread    +----------+           |  r   |
+----------+       |(Worker)  |                      |  y   |
                   +----+-----+     +-------+        |      |
                        |     +---- | Stack +--------+      |
                        |           +-------+        +------+
                        |
                        |           +---------+      +------+
                        +-----------+Register +------+CPU   |
                                    +---------+      +------+

```

## Java线程池的相关参数
```
public class ThreadPoolExecutor extends AbstractExecutorService {
    ...
    private volatile int maximumPoolSize;
    private final HashSet<ThreadPoolExecutor.Worker> workers;
    ...
}
```
workers就是一组工作者线程，而maximumPoolSize则限制了
工作者线程的数量。当然为了控制的更加精细，Java的线程池还
提供了很多其他的参数，这些就不是本文的重点了。

## 参考资料
- https://blog.csdn.net/woshiluoye9/article/details/60779752
- https://juejin.im/post/5c8896be5188257ec828072f