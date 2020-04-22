# 如何重载hashcode？

## 场景
- 自定义对象当做hashmap的key时。
- 自定义对象放入hashset时。

以上二种场景都需要重载equals方法，所以也需要重载hashCode方法。因为
在hashmap内部查找时，是先通过hashcode定位，再比较equals，需要保证equals相等时hashcode必须相等。

## 目标
提高查询/定位效率。

## 方案
通过把一个对象映射成一个整数(hashcode)索引。

## 实现要点
hashcode算法必须满足以下几点：
* 散列： 平均分布，减少冲突。
* 一致性： 
  * equals相等，hashcode必相等。
  * 同一个实例对象，hashcode最好保持不变。
* 效率：生成hashcode是为了提高查询效率，所以生成算法必须简单。
* 越界问题。hashcode为整型，需要考虑一下计算越界问题。

## 解决方案
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