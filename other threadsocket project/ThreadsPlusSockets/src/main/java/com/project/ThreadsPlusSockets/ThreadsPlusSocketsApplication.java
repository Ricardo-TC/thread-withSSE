package com.project.ThreadsPlusSockets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin(origins = "*", allowedHeaders = "*")
@ComponentScan(basePackages = {"com.project.ThreadsPlusSockets","Context"})//,"Context"}
@EnableJpaRepositories(".com.project.ThreadsPlusSockets.entity.repository")
public class ThreadsPlusSocketsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ThreadsPlusSocketsApplication.class, args);
	}

}
