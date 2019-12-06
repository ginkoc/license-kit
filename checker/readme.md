# Checker

checker是一个导出api，使用了`aspectj`的`ajc`编译器将检查代码织入使用了checker切面的方法上。checker切面负责校验manager生成的license的参数，用于限制使用产品（软件）的某些行为。

------

## 导出checker Api

由于该项目依赖于commont项目，所以要运行该项目需要先`install`common到本地maven仓库，在common目录下执行：

```shell
mvn clean install
```

 接着在本项目下（checker目录）执行：

```shell
mvn clean package
```

执行完成后你会在manager/target目录下看到`checker-0.0.1-SNAPSHOT-jar-with-dependencies.jar`和`checker-0.0.1-SNAPSHOT-jar`分别是包括依赖jar和不包括依赖jar的导出JAR。

## 使用checker

//todo

