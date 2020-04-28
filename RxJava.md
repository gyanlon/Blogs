#如何理解响应式编程RxJava？
## 什么是RxJava？
RxJava是一种基于观察者模式的响应式编程框架。

举个简单的例子，一般风格：
- System.out.println("abc");

RxJava响应式编程风格：
- Observable.just("abc").subscribe(System.out::println);

我们先来看一个这个一般风格，它包含三部分：
```
+---------+ +-----+ +---+
|System.out.println("abc");
+----+----+ +--+--+ +-+-+
     |         |      |
+----+----+ +--+--+ +-+---+
| 输      | | 打  | | 数  |
| 出      | | 印  | | 据  |
| 设      | |     | |     |
| 备      | |     | |     |
+---------+ +-----+ +-----+
```

可以看出上述语句的意思是"输出设备",打印出“数据”。它的主对象是设备，是从输出设备
的视角来看待这个操作的。

那么如果我们改变一下视角，从数据的角度来看待这条语句呢？

以“数据”为主对象。我们用一段伪代码来改写一下这条语句。

“abc”.printedBy(System.out);

但是这里打印这个动作应该是属于设备的行为，
继续改进一下这段伪代码：
```
String("abc").call(System.out, "println");
------------       ----------  ----------
     |                  |          |
数据生成器           输出设备     动作   

对上述伪代码再进一步抽象如下：
DataProducer("abc").call(DataPrintConsumer);
------------------        ---------- 
     |                        |   
  生产者                    消费者      
  发布者                    订阅者
  被观察者                  观察者
```
这其实就是一个观察者模式嘛，或者叫订阅模式，或者叫生产者消费者模式。
再来看看这条语句Observable.just("abc").subscribe(System.out::println);
是不是好理解一些了呢？

## 参考列表
https://www.jianshu.com/p/69a6ae850736
https://www.jianshu.com/p/88aacbed8aa5

https://www.geeksforgeeks.org/system-out-println-in-java/
https://blog.csdn.net/lipinganq/article/details/53427102