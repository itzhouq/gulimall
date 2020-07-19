package com.atguigu.gulimall.member;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.atguigu.gulimall.member.dao")
public class GulimallMemberApplication {
	public static void main(String[] args) {
		SpringApplication.run(GulimallMemberApplication.class, args);
	}
}
