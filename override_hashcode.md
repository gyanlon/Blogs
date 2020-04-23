# 如何重载hashcode？

## hashcode是什么？
在Java对象中，hashcode可以看作是每个对象实例的一个整型索引。有两个特点：
- 散列
- int型

## 为什么需要hashcode?
- 当比较两个对象时，比较属性的值比较耗时，先比较hashcode能够节省计算时间。
- 整型易于定位。
- 散列不易冲突。

## 使用场景
- hashmap中key的定位。

## 重载场景
- 自定义对象当做hashmap的key时。
- 自定义对象放入hashset时。

## 为什么需要重载
因为默认equals比较的时引用的值（对象实例的内存地址）。而实际场景中
需要比较的是对象中的属性值。所以需要重载equals方法，因而也需要重载hashCode方法。因为
在hashmap内部查找时，是先通过hashcode定位，再通过equals比较，需要保证equals相等时hashcode必须相等。

## 重载hashcode的目标
提高查询/定位效率。

## 方案
重载hashCode方法，把对象值映射成一个整数值(hashcode)。

## 实现要点
hashcode算法必须注意以下几点：
* 散列： 平均分布，减少冲突。
* 一致性： 
  * equals相等，hashcode必相等。
  * 同一个实例对象，hashcode需要保持不变。
* 效率：生成hashcode是为了提高查询效率，所以生成算法兼顾效率。
* 越界问题。hashcode为整型，需要考虑计算越界问题。

## 针对要点的解决方法
- equals相等，hashcode必相等: 最好使用和计算equals相同的字段或者其子集。
- 同一个实例对象，hashcode最好保持一致： 建议使用不变的字段值来计算hashcode。
所以这需要你将参与hashcode计算的字段做成不变的，只在创建时赋值，不提供更新入口方法。
具体例子可参考String/Integer类的hashcode算法（其参入计算hashcode的value值都是immutable的）。
- 越界问题, int型越界以后，符号位会变（比如正变负），但不影响hashmap定位, 因为hashmap最大容量为(1>>>30)，过滤掉了符号位的影响。

## 优化

## 参考资料
https://www.sitepoint.com/how-to-implement-javas-hashcode-correctly/
https://blog.csdn.net/u012961566/article/details/72963157
https://javaconceptoftheday.com/how-the-strings-are-stored-in-the-memory/