#为什么使用Base64？

##什么是Base64?
  是一种基于64个可打印字符来表示二进制数据的表示方法。
在MIME格式的电子邮件中，使用的字符包括大小写拉丁字母各26个、数字10个、加号+和斜杠/，共64个字符，
在日常使用中我们还会看到“=”或“==”号出现在Base64的编码结果中，“=”在此是作为填充字符出现.

##为什么要使用Base64？
我们都知道计算机系统之间通过二进制字节流传输数据，
这就需要双方有对应的编码/解码器。
开始的时候，出现了很多不同编码/解码标准，也出现了相应的兼容问题。

比如email系统,设计之初就被定义为基于文本的格式进行传输，
它会把收到的字节流翻译成文本。

普通文本没有问题，但是当传输图片时，对于这部分字节流，
翻译成文本就会出现特殊字符，有些email网关会把某些特殊字符擦除（安全考虑），
进而导致接字节流受损。

为了解决上述问题，作为MIME多媒体电子邮件标准的一部分—base64被开发出来。

它把图片二进制字节流映射成了64个最常用的字符，来兼容一些基于文本格式传输的系统。

TEXT => bytes[] => BASE64 => bytes[]  ==========> 


## 扩展
对于URL的回调URL，需要进行url-safe Base64编码，其中的+, /变成-，_。

## 参考资料
https://blog.csdn.net/wo541075754/article/details/81734770
https://www.liaoxuefeng.com/wiki/1016959663602400/1017684507717184
https://www.cnblogs.com/kidsitcn/p/6901431.html
