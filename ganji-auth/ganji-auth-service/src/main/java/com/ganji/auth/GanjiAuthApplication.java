package com.ganji.auth;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.stereotype.Service;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class GanjiAuthApplication {
}
