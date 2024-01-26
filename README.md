# springboot 配置文件内容加密

## 1. 导入依赖
```xml
  <dependency>
      <groupId>com.github.ulisesbocchio</groupId>
      <artifactId>jasypt-spring-boot-starter</artifactId>
      <version>3.0.5</version>
  </dependency>
```
> 他还有很多灵活的配置，但我这里并不需要，全部使用默认即可。如果想了解更多用法，就去自行查阅。


## 2. 使用测试类将密码加密
```java
/**
 * @author fu yuan hui
 * @since 2024-01-25 09:23:23 Thursday
 */
@Slf4j
public class JasyptTest {

    /**
     * 这个必须和配置文件里面的保持一致！！！！！！！！！！！！
     */
    private static final String SALT = "EftEbfYkitulv73I2p0mXI50h1K0_kA1@eVmAe45jhc9c1E0`aA0(kZ-0)cG2+jE";

    /**
     * 配置文件里面的写法是：
     * <p>
     *  jasypt:
     *    encryptor:
     *      password: EftEbfYkitulv73I2p0mXI50h1K0_kA1@eVmAe45jhc9c1E0`aA0(kZ-0)cG2+jE
     * </p>
     *
     * 当我用test1()方法(使用的都是默认配置）将密码加密后，然后解密的时候提示，秘钥不匹配？？？
     * 当我用test2()方法进行加密后，就可以解密了，懒得探究，以后加密就可以用我下面的test2()
     */
    public static void main(String[] args) {

        //该方法解密后，启动程序无法解密
        //test1();

        test2();
    }

    public static void test1() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(SALT);
        config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        config.setPoolSize("1");
        encryptor.setConfig(config);

        String nacos = encryptor.encrypt("nh**2023##");
        log.info("加密后的密文：{}", nacos);

        String decrypt = encryptor.decrypt(nacos);
        log.info("解密后的明文：{}", decrypt);
    }

    public static void test2() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(SALT);
        config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setProviderClassName(null);
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);

        String nacos = encryptor.encrypt("nh**2023##");
        log.info("加密后的密文：{}", nacos);

        String decrypt = encryptor.decrypt(nacos);
        log.info("解密后的明文：{}", decrypt);
    }
}
```


## 3.用加密后的密码替换明文密码（我这里用nacos测试）
```yaml
spring:
  application:
    # 服务名称
    name: eft-system-auth
  profiles:
    # 环境配置
    active: @spring.profile@
  cloud:
    nacos:
      ############# 将用户名和密码加密，用ENC()包裹，默认前缀是ENC(, 后缀是), 都可以改的。
      username: ENC(BxiyTu1mbvQeaMbhQGrvosNFzd56SRnjs5OQ8BBvg1PacP8JzCHIi+LRdgTqphtn)
      password: ENC(rZj0kWhkEPZ48B8X7vZNJu1Jvi/Nv/UuUZUWoKljAnRcUfgmE8oCJptLkxNDRk88)
      discovery:
        # 服务注册地址
        server-addr: ${server.nacos}
        namespace: ${NACOS_NAMESPCE:${server.nacos-namespace}}
      config:
        # 配置中心地址
        server-addr: ${server.nacos}
        namespace: ${NACOS_NAMESPCE:${server.nacos-namespace}}
        # 配置文件格式
        file-extension: yml
        # 共享配置
        shared-configs:
          - eft-${spring.profiles.active}.${spring.cloud.nacos.config.file-extension}
          # 数据库、redis共享配置文件
          - eft-mysql-redis-${spring.profiles.active}.${spring.cloud.nacos.config.file-extension}
```


## 3.引导类bootstrap.yml配置文件导入秘钥
```yaml
#将秘钥和加密后的密码放到一起？似乎也不太安全吧，但是我看几个系统都是这么用的
jasypt:
  encryptor:
    password: EftEbfYkitulv73I2p0mXI50h1K0_kA1@eVmAe45jhc9c1E0`aA0(kZ-0)cG2+jE
```
> 否则解密的时候因为找不到秘钥而无法解密从而报错。
![img.png](eft-system-auth%2Fsrc%2Fmain%2Fresources%2Fimg%2Fimg.png)
### 3.1 秘钥和加密后的密码放到一起？
从上面可以看到，秘钥和加密后的密码放到了一起，这岂不是失去了加密的意义？ 但是我尝试不放到这个文件中是不行的（当然他还可以指定从文件中读取），是不是从文件中读取会好一点？ 我尝试将秘钥放到nacos的共享配置文件中，但是
都是无法读取的，后面我看有人说，可以将其放到环境变量中。像这样：
 ```java
    java -jar xxx.jar --jasypt.encryptor.password=EftEbfYkitulv73I2p0mXI50h1K0_kA1@eVmAe45jhc9c1E0`aA0(kZ-0)cG2+jE


   IDEA启动配置虚拟机参数：
   -Djasypt.encryptor.password=EftEbfYkitulv73I2p0mXI50h1K0_kA1@eVmAe45jhc9c1E0`aA0(kZ-0)cG2+jE
 ```

<hr>
这里就可以启动测试了，看看服务能否正常连接nacos
<hr>

## 4.将nacos里面的mysql密码加密
1. 只需要将mysql密码用上面的测试类加密后，然后放到这里即可。<br>
![img_1.png](eft-system-auth%2Fsrc%2Fmain%2Fresources%2Fimg%2Fimg_1.png) <br><br>
2. <b><font color="red">无需在nacos的配置文件中再次引入秘钥，他会读取bootstrap.yml文件中的。</font></b>


## 5.引入后出现的问题
### 5.1 日志不正常输出
以前项目中可以正常输出的自定义日志，现在全部都不输出了，后面想着排除`jasypt-spring-boot-starter`里面的日志，但是都没有用，后面就做了这样的调整：<br>
```yaml
logging:
  level:
    com.qiuguan.jasypt: info  #让我项目中自定义的日志以INFO级别输出
    com.alibaba.nacos: warn   #我看nacos打印一些INFO级别的敏感信息，于是我调整为WARN,不让她输出
    com.ulisesbocchio.jasyptspringboot: warn  #同上
```

### 5.2 bootstrap.yml文件无法解析用@符号读取的pom文件中的配置
![img_2.png](eft-system-auth%2Fsrc%2Fmain%2Fresources%2Fimg%2Fimg_2.png) <br>

在pom.xml文件中增加如下配置：
```xml
    <build>
        <!--不然无法解析 bootstrap.yml配置文件中用@读取的pom配置-->
        <resources>
            <resource>
                <directory>src/main/resources</directory>
                <filtering>true</filtering>
            </resource>
        </resources>
    </build>

```

### 5.3 打成jar后无法找到主清单
在pom.xml文件中增加如下配置：
```xml
 <build>
        <finalName>${project.artifactId}</finalName>
        <!--不配置这个的话，打成jar包启动会报：没有主清单 -->
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>2.6.6</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>repackage</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```