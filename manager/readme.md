# Manager

------

manager是一个后台项目，基于`truelicense`、`redis`以及`java keytool`工具实现license证书的生成和下载功能。集成了`swagger-io`，方便查看和测试api接口。

逻辑比较简单，只提供了license生成和下载接口，不负责license数据的记录，所有license相关的信息应该由调用方进行记录。license的生命周期如下：

- 生成license文件后会返回一个uuid口令，用于下载指定的license；
- 口令的有效时间为5分钟，5分钟以后将无法通过口令下载license；
- 口令失效一分钟以后license会从服务器被删除。

------

## 上手指南

### 运行条件

- jdk-8 / openjdk-8-jdk
- redis
- maven-3

### 项目配置

目前项目的逻辑比较简单，项目的可配置项并不多。只要在`application.yml`文件中配置项目端口号以及redis相关配置即可，配置如下：

```yaml
server:
    port: 8686

spring:
    redis:
      host: localhost
      port: 6379
      password: mypass
      lettuce:
        pool:
          max-active: 8
          max-wait: -1ms
          max-idle: 8
          min-idle: 0
```

注意：redis的password属性要与实际的redis密码相同

### 运行项目

由于该项目依赖于common项目，所以要运行该项目前，需要先将common包安装到本地maven仓库，在common项目下（common目录）下执行：

```shell
mvn clean install
```

- 使用maven插件运行项目

```shell
 mvn spring-boot:run
```

如果你需要以`debug`模式来启动项目，可以添加以下参数

```shell
mvn spring-boot:run -P debug
```

- 生成jar包后运行项目

在本项目下（manager目录）执行：

```shell
  mvn clean package
```

执行完成后你会在manager/target目录下看到`license-manager-0.0.1-SNAPSHOT.jar`可执行jar包。通过执行以下命令启动服务：

```shell
  java -jar license-manager-0.0.1-SNAPSHOT.jar
```

### 查看服务接口

运行该项目以后，可以在swagger界面查看服务接口，地址如下：

```url
ip:port/swagger-ui.html
```

在swagger界面可以看到，接口调用的url以及所需要的参数，并且对参数进行了说明。

在创建license时，需要注意指定的`subject`参数，代表需要被许可的实体（可以是一款产品的名字或者软件的代号），在使用license时需要用到，因此需要记录你指定`subject`参数的值。

调用创建license接口后会返回一个uuid作为下载令牌，用于下载指定的license。令牌的有效时间为5分钟，5分钟以后令牌将会失效，令牌失效一分钟以后服务端会删除license文件。





