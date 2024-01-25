package com.qiuguan.jasypt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author fu yuan hui
 * @since 2024-01-25 16:03:27 Thursday
 */
@Slf4j
@SpringBootApplication
public class AuthApplication {

    public static void main(String[] args) {

        SpringApplication.run(AuthApplication.class, args);
        log.info("Auth application 服务启动成功!!!");
    }
}
