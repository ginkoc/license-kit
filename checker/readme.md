# Checker

checker是一个导出api，作用是辅助用户对license的内容进行校验。checker使用了`aspectj`的`ajc`编译器将检查代码织入checker检查点（使用的`@CheckPoint`注解的方法），对于具有检查点的方法，在执行时检查点方法时会校验当前环境中的参数与manager生成的license的参数是否匹配，当两者不匹配时会抛出`CheckException`异常，方便使用者统一处理，从而达到限制产品（软件）的某些行为的目的。

------

## 导出checker Api

由于该项目依赖于commont项目，所以要运行该项目需要先`install`common到本地maven仓库，在common项目下（common目录）下执行：

```shell
mvn clean install
```

 接着在本项目下（checker目录）执行：

```shell
mvn clean package
```

执行完成后你会在checker/target目录下看到`checker-0.0.1-SNAPSHOT-jar-with-dependencies.jar`和`checker-0.0.1-SNAPSHOT-jar`分别是包括依赖jar和不包括依赖jar的导出JAR，使用哪一个jar包取决于使用者的环境。

## 使用checker

#### 1.添加依赖

- 传统项目

	将checker以及相关依赖加入`lib`目录

- Maven项目，添加以下依赖：

```xml
<dependency>
    <groupId>com.ginko.license</groupId>
    <artifactId>checker</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

#### 2.初始化LicenseContentHolder

`LicenseContentHolder`是装载license中定义的参数的容器，用于校验当前环境的中参数是否正确。该对象初始化需要以下4个参数：

- subject

	证书主体，可以是一款产品的名字或者软件的代号。必须与证书生成时指定的`subject`参数一致。

- cipher

	口令，即生成license后，返回的用于下载的uuid令牌。

- storePath

	公钥库路径，包含在下载获得的`.zip`文件中，里面存储了用于解密license文件的公钥。

- licenseFile

	license文件路径，包含在下载获得的`.zip`文件中，存储加密后的控制参数信息的文件。

------

**初始化方式**

- 手动初始化

可以通过手动调用的方式，自己控制LicenseContentHolder初始化的时机，例如使用`@PostConstruct`注解。

```java
public class OnSystemSetup {
    @PostConstruct
    public void onSetup() {
        String subject;
        String cipher;
        String licensePath;
        String storePath;
        LicenseContentHolder.INSTANCE.install(subject, cipher, licensePath, storePath);
    }    
}
```

- 通过辅助类`LicenseInitHelper`初始化

辅助类的目的是方便在**web环境**中使用，这种方式需要结合`properties`文件进行使用，在`properties`中需要定义subject、cipher以及path的值。其中path为公钥库和license文件的公共父目录，并且path对应的目录下只能存在一个公钥库和一个license文件。

在spring环境中，可以通过配置类形式实现初始化：

```java
@Configuration
public class LicenseConfig {
    @Bean
    public ServletListenerRegistrationBean servletListenerRegistrationBean(){
        ServletListenerRegistrationBean<ServletContextListener>
                servletListenerRegistrationBean = new ServletListenerRegistrationBean<>();
		
        // 以'classpath:'代表从jar包的classes目录下读取配置文件，否则表示从文件系统读取配置文件
        String properties = "classpath:license.properties";
        servletListenerRegistrationBean.setListener(new LicenseInitHelper(properties));
        return servletListenerRegistrationBean;
    }
}
```

在非spring环境中，可以通过配置`web.xml`文件实现初始化：

```xml
<!--web.xml-->
<web-app>
    <context-param>
            <!-- param-name固定为licenseProperties -->
            <param-name>licenseProperties</param-name>
            <!-- param-value为propperties文件位置 -->
            <param-value>/path/of/license-config.properties</param-value>
    </context-param>
    <listener>
        <!-- listener-class为LicenseInitListener用于启动时加载license相关配置 -->
        <listener-class>com.ginko.license.checker.core.LicenseInitHelper</listener-class>
    </listener>
</web-app>
```

#### 3.设置检查点

在需要进行参数检查的方法上使用`@CheckPoint`注解来设置检查点，通过设置`@CheckPoint`的predicates属性设置需要进行检查的条目，predicates是`LicensePredicate`接口实现类的集合，下面是一个设置检查点的例子。

```java
@Controller
public class ExampleController {

    @GetMapping(value = "/test")
    @CheckPoint(predicates = {DatePredicate.class, MyPredicate.class})
    @ResponseBody
    public Map<String, String> testingChecker() {
        Map<String, String> map = new HashMap<>();
        map.put("result", "all predicates pass");
        return map;
    }
}
```

另外，可以通过实现`LicensePredicate`接口或者继承`AbstractLicensePredicate`抽象类来自定义一个检查条目，例如：

```java
public class MyPredicate extends AbstractLicensePredicate {

    @Override
    public boolean test() {
        Optional<String> value = getContentValueByType(LicenseContentType.IP);
        String exceptIp = "127.0.0.1";
        // 如果没有设置ip属性，说明不对ip进行限制
        return !(value.isPresent() && !exceptIp.equals(value.get()));
    }
}
```

如果出现检查点校验失败的情况，系统会抛出`CheckException`异常，可以通过捕获该异常进行进一步的处理。

#### 4.织入切面

实际上checker只是定义了切面以及切面校验的逻辑，对于切面逻辑的织入还是需要通过配置或者执行命令的方式实现。为了避免对于容器的依赖，没有选择使用jdk动态代理或者cglib动态代理，而是使用`AspectJ`的ajc编译器进行织入的方式。

编译后织入，实际上就是使用javac对源代码进行编译后，再使用ajc编译器对class文件进行处理，从而织入切面代码。织入方式大体上有2种：

- Maven插件

对于maven项目，可以使用maven的aspectj插件实现代码织入。

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>aspectj-maven-plugin</artifactId>
            <version>1.10</version>
            <configuration>
                <source>1.8</source>
                <target>1.8</target>
                <complianceLevel>1.8</complianceLevel>
                <aspectLibraries>
                    <!-- 作为切面的库 -->
                    <aspectLibrary>
                        <groupId>com.ginko.license</groupId>
                        <artifactId>checker</artifactId>
                    </aspectLibrary>
                </aspectLibraries>

                <!-- 如果使用了lombok就必须增加以下配置 -->
                <!--<forceAjcCompile>true</forceAjcCompile>
                 <sources/>
                 <weaveDirectories>
                  <weaveDirectory>${project.build.directory}/classes</weaveDirectory>
                 </weaveDirectories>-->
                <!-- 如果使用了lombok就必须增加以上配置 -->
            </configuration>
            <executions>
                <execution>
                    <goals>
                        <goal>compile</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

- 手动执行java命令

对于非maven项目而言，可以通过执行java命令的方式实现代码织入。此时就要依赖于`aspectjrt.jar`以及`aspectjtools.jar`两个jar包，可以通过maven下载获得。执行的命令如下（建议通过脚本实现）：

```shell
java -jar $ASPECTJ_TOOLS -cp $MY_ASPECT:$ASPECTJ_RT -source 1.8 -injars  $IN_JAR -outjar $OUT_JAR -aspectpath $MY_ASPECT

#$ASPECTJ_TOOLS为aspectjtools.jar所在位置
#$ASPECTJ_RT为aspectjrt.jar所在位置
#$MY_ASPECT为要织入的切面所在的jar包,即我们的checker包
#$IN_JAR为源码编译后生成的jar包
#$OUT_JAR为织入切面以后的jar包名
#注意java之间的分割符，在linux中为':',而在windows中为';'
```

上面的命令在有外部依赖jar时，会导致织入切面的jar包中依赖jar被压缩，从而使jar包被破坏。由于目前没有找到不进行压缩的方法，因此只能转换思路，先解压源码jar包，然后指定源码中需要进行切面织入的类，最后重新打包成jar包。如下：

```shell
unzip $IN_JAR -d $FILE_PATH

java -jar $ASPECTJ_TOOLS -cp $MY_ASPECT:$ASPECTJ_RT -source 1.8 -inpath $CLASSES -d $CLASSES -aspectpath $MY_ASPECT

jar -cfM0 $OUT_JAR $FILE_PATH/*

#$ASPECTJ_TOOLS为aspectjtools.jar所在位置
#$ASPECTJ_RT为aspectjrt.jar所在位置
#$MY_ASPECT为要织入的切面所在的jar包,即我们的checker包
#$IN_JAR为源码编译后生成的jar包
#$OUT_JAR为织入切面以后的jar包名
#$FILE_PATH为jar包解压的内容所在的路径
#$CLASSES为$FILE_PATH要进行切面的织入的class类所在的路径
#注意java之间的分割符，在linux中为':',而在windows中为';'
```

### 参考项目

详见[example项目](example)