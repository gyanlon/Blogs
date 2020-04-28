
## FutureTask
FutureTask类似于一个工作者线程状态机，根据线程不同的状态，执行不同的逻辑。

    private Object outcome; //储存返回结果
    private volatile Thread runner; // 工作者线程
    
    run方法执行完将结果放入outcome
    awaitDone方法会一直循环监控当前线程状态，如果完成，则返回完成状态。
    get方法在awitDone执行完后，返回outcome

