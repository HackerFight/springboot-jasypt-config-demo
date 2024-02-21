package com.qiuguan.jasypt.controller;

import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fu yuan hui
 * @since 2024-01-26 13:29:53 Friday
 */
@RestController
public class AuthController implements SmartInitializingSingleton {


    /**
     * 这个密码已经在 ft-mysql-redis-dev.yml 配置文件加密中加密了：
     * <p>
     * spring:
     *   datasource:
     *     name: dev
     *     url: jdbc:mysql://192.168.6.59:3306/one-record-dev?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
     *     username: root
     *     password: ENC(RO8gaCKEBwmGFEzRI1THPpE9JYrshMThKERwhWVUFPF8TgMab3g3Hrg8cUMqBhBf)
     *     type: com.alibaba.druid.pool.DruidDataSource
     *     driver-class-name: com.mysql.cj.jdbc.Driver
     * </p>
     *
     *
     * 这个mysql的密码只需要将明文用 {@link JasyptTest} 类加密一下，然后用ENC(XXX) 包裹一下即可。“无需“在nacos的配置文件中在配置下面这个：
     * <pre>
     *     jasypt:
     *       encryptor:
     *         password: EftEbfYkitulv73I2p0mXI50h1K0_kA1@eVmAe45jhc9c1E0`aA0(kZ-0)cG2+jE
     * </pre>
     *
     * 因为我已经在bootstrap.yml中配置了，所以他已经全局都可用了。或者用环境变量：--jasypt.encryptor.password=EftEbfYkitulv73I2p0mXI50h1K0_kA1@eVmAe45jhc9c1E0`aA0(kZ-0)cG2+jE
     */
    @Value("${spring.datasource.password}")
    private String mysqlPassword;


    @GetMapping("/getpwd")
    public String getPassword() {
        return "mysql password: " + mysqlPassword;
    }

    @Override
    public void afterSingletonsInstantiated() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> mysqlPassword = " + mysqlPassword);
    }
}
