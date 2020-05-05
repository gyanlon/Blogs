# RxJava的线程切换Observable.subscribeOn

## 什么是subscribeOn?

是Observable的一个方法，它通过将数据生成逻辑委托给一个调度器来实现数据的异步生成。

## 测试

情况一：不切换线程
```
        Observable.just(1).doOnSubscribe(() -> {
            System.out.println("subscribe in thread:" + " " + Thread.currentThread().getName());
        }).subscribe((t) -> {
            System.out.println("consume in thread:" + Thread.currentThread().getName());
        });
```
结果：
```html
subscribe in thread: main
consume in thread: main
```

情况二：切换线程(异步生产数据)
```
        Observable.just(1).doOnSubscribe(() -> {
            System.out.println("subscribe in thread:" + " " + Thread.currentThread().getName());
        }).subscribeOn(Schedulers.newThread()).subscribe((t) -> {
            System.out.println("consume in thread:" + Thread.currentThread().getName());
        });
```
结果：
```html
subscribe in thread: RxNewThreadScheduler-1
```
可以看出，由于数据生成切换到新的线程异步执行，main线程的消费并未被触发。

情况三：切换线程(异步生产数据)+阻塞消费
```
        Observable.just(1).doOnSubscribe(() -> {
            System.out.println("subscribe in thread:" + " " + Thread.currentThread().getName());
        }).subscribeOn(Schedulers.newThread()).toBlocking().subscribe((t) -> {
            System.out.println("consume in thread:" + Thread.currentThread().getName());
        });
```
结果：
```html
subscribe in thread: RxNewThreadScheduler-1
consume in thread:main
```
可以看出，数据生成逻辑异步执行在其他线程中，然后通过toBlocking在切换到主线程进行消费。

## 参考资料

https://www.jianshu.com/p/3e5d53e891db