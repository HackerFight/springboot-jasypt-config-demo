package com.qiuguan.jasypt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author fu yuan hui
 * @since 2024-01-25 16:02:12 Thursday
 */
@Slf4j
@SpringBootApplication
public class NeServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NeServerApplication.class, args);
        log.info("NeServer application 服务启动成功!!!");
    }
}
