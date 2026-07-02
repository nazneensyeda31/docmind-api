package com.docmind.docmind_api;

import org.springframework.boot.SpringApplication;

public class TestDocmindApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(DocmindApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
