package com.docmind.docmind_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DocmindApiApplication {

	public static void main(String[] args) {SpringApplication.run(DocmindApiApplication.class, args);}

}
