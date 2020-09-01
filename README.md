# ButterKnifeReflect
使用Apt实现一个ButterKnife Ioc容器，ButterKnife属于编译期注解，运行时使用反射调用对应Activity的View Bind。为了提升运行时性能和效率，ButterKnife做了类缓存。

* 使用谷歌AutoService注解，完成Processor在META-INF services目录下的自动注册
* 生成代码使用StringBuilder拼接，非常容易出错，可以考虑使用JavaPoet
* 注解处理器的process方法会被多次调用，可根据elements的size减少重复调用

```java
       //获取所有注解element
       Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(BindView.class);
       if (elements.size() <= 0) {
            return false;
        }
```

## 项目结构
![image](https://github.com/wangyuetingtao/ButterKnifeReflect/blob/master/screenshot/WX20200831.png)

## 代码生成目录
![image](https://github.com/wangyuetingtao/ButterKnifeReflect/blob/master/screenshot/WX20200831-182310.png)

## 生成代码在dex 文件中的结构
![image](https://github.com/wangyuetingtao/ButterKnifeReflect/blob/master/screenshot/WX20200831-182643.png)


注意点：
  
远程调试参考：[如何调试Android Annotation Processor程序](https://blog.csdn.net/ShuSheng0007/article/details/92795576)
