package com.qiuguan.jasypt.test;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

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
