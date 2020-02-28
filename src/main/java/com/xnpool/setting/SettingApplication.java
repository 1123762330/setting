package com.xnpool.setting;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.xnpool.setting.domain.mapper")
public class SettingApplication {


    public static void main(String[] args) {
        SpringApplication.run(SettingApplication.class, args);
    }

}
