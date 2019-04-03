package com.alog.dataservice.gateway;

import com.alog.dataservice.gateway.filter.AuthFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
@EnableZuulProxy
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }

    @Bean
    public AuthFilter authFilter(){
        return new AuthFilter();
    }
}
