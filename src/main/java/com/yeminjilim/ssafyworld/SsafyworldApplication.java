package com.yeminjilim.ssafyworld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;

@EnableR2dbcAuditing
@SpringBootApplication
public class SsafyworldApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsafyworldApplication.class, args);
    }

}
