package com.poten.hoohae;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableCaching
public class HoohaeApplication {
	public static void main(String[] args) {
		SpringApplication.run(HoohaeApplication.class, args);
	}

}
