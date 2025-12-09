package com.online_shopping_project.onlineshopping;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class OnlineShoppingApplication {

    @Value("${spring.web.resources.static-locations:未读取到配置}")
    private String staticLocations;

    public static void main(String[] args) {
        SpringApplication.run(OnlineShoppingApplication.class, args);
    }

    @PostConstruct
    public void printConfig() {
        System.out.println("🌍 static-locations = " + staticLocations);
        System.out.println("📁 工作目录 = " + System.getProperty("user.dir"));
    }

}
