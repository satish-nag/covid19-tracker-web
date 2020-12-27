package com.covid19tracker.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true,proxyTargetClass = true)
public class Covid19TrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(Covid19TrackerApplication.class, args);
    }

}
