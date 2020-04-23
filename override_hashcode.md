# Java随想：如何重载hashCode方法？

## 什么是hashcode？
hashcode即散列码，一般用于提高查询/定位效率。
在Java对象中，hashcode可以看作是每个对象实例的一个整型索引。有三个特点：
- 散列
- int型
- 一致性 

默认值是对象实例的内存地址。典型应用场景，比如hashmap中key的定位。

## 为什么这么设计hashcode?
- 整型易于定位。
- 散列不易冲突。
- 一致性，同一个实例对象，hashcode需要保持不变，以保持定位的稳定性。

## 为什么需要重载？
1) 默认equals比较的是引用（对象实例的内存地址）。
而实际场景中需要比较的是对象中的属性值。所以需要重载equals方法。
2) 在hashmap查找key时，是先hashcode定位，再equals比较，
这就意味着hashcode相等的情况下，equals不一定相等。而equals相等，则
hashcode必相等，所以重载eqauls时，必须重载hashCode，以保证hashmap这套定位机制的正确执行，

所以在一下场景下会需要用到重载equals&hashCode方法？
- 自定义对象当做hashmap的key。
- 自定义对象放入hashset。

## 实现要点
hashcode算法必须注意以下几点：
* 散列： 平均分布，减少冲突。
* 一致性： 
  * equals相等，hashcode必相等。
  * 同一个实例对象，hashcode需要保持不变。
* 效率：hashcode是为了提高查询效率，所以生成算法需要兼顾效率。
* 越界问题。hashcode为整型，需要考虑计算越界问题。

## 针对要点的解决方法
- equals相等，hashcode必相等: 最好使用和计算equals相同的字段或者其子集。
- 同一个实例对象，hashcode最好保持一致： 建议使用不变的字段值来计算hashcode。
所以这需要你将参与hashcode计算的字段做成不变的，只在创建时赋值，不对外提供更新此属性的入口。
具体例子可参考String/Integer类的hashCode方法（其类本身就是immutable的）。
- 越界问题, int型越界以后，符号位会变（比如正变负），但不影响hashmap定位, 因为hashmap最大容量为(1>>>30)，过滤掉了符号位的影响。

## 优化

## 参考资料
https://www.sitepoint.com/how-to-implement-javas-hashcode-correctly/
https://blog.csdn.net/u012961566/article/details/72963157
https://javaconceptoftheday.com/how-the-strings-are-stored-in-the-memory/