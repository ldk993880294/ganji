package com.ganji.zuul.comfig;


import org.apache.catalina.filters.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter(){
        return new CorsFilter();
    }
}
