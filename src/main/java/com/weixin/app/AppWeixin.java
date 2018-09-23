package com.weixin.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = { "com.weixin" })
@SpringBootApplication
public class AppWeixin extends SpringBootServletInitializer {
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AppWeixin.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(AppWeixin.class, args);
    }

}
