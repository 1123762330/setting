package com.xnpool.setting;


import com.xnpool.logaop.util.WriteLogUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients //Feign
@EnableEurekaClient
@SpringBootApplication
@MapperScan("com.xnpool.setting.domain.mapper")
public class SettingApplication {

    @Bean
    public WriteLogUtil getWriteLog(){
        return new WriteLogUtil();
    }

    public static void main(String[] args) {
        SpringApplication.run(SettingApplication.class, args);
    }

}
