package com.asugar.shorturl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class ShortUrlApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShortUrlApplication.class, args);
    }

}
