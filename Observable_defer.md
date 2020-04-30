# 如何理解RxJava中的Observable.defer?

## 什么是Observable?

Observable是一个对象，代表着被观察的数据方， 数据发布者，数据产生者，它会产生数据供消费者消费。
看通过这边文章简单理解之:
https://blog.csdn.net/coobee/article/details/105817994

## 什么是Defer
Defer是延迟的意思。

Observable.defer是通过**延迟创建数据生产者(Observable)**的方式**推迟数据生产**的时间。
直到注册的时候才开始生产数据.

通过如下代码来测试一下使用了defer之后的效果。
```
import rx.Observable;

public class Test {

    private static String dataProducer() {
        String str = "Hello,RxJava!";
        System.out.println("Produce Data :" + str);
        return str;
    }

    private static void just() {
        Observable.just(dataProducer()).doOnSubscribe(()->{
            System.out.println("Subscribe!");
        }).subscribe(s->{
            System.out.println("Consume Data :" + s);
        });
    }
    private static void defer() {
        Observable.defer(()->{
            return Observable.just(dataProducer());
        }).doOnSubscribe(()->{
            System.out.println("Subscribe!");
        }).subscribe(s->{
            System.out.println("Consume Data :" + s);
        });
    }

    public static void main(String [] args) {
        just();
        defer();
    }
}

```

可以看出，正常情况是先产生数据，再注册，再消费。
```
Produce Data :Hello,RxJava!
Subscribe!
Consume Data :Hello,RxJava!
```
而使用了defer之后，是先注册，再生产数据，再消费。
```
Subscribe!
Produce Data :Hello,RxJava!
Consume Data :Hello,RxJava!
```

## 参考资料
http://reactivex.io/documentation/operators/defer.html
