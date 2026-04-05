package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}

/*
- 스프링 부트 실행
- 톰캣 서버 실행
- 스프링이 관리할 클래스들 스캔 시작
 */