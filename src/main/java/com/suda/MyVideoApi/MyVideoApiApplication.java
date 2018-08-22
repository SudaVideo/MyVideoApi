package com.suda.MyVideoApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MyVideoApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyVideoApiApplication.class, args);
	}
}
