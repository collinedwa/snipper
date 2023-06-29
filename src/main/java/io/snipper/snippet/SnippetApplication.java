package io.snipper.snippet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"io.snipper.snippet.controller"})
public class SnippetApplication {

	public static void main(String[] args) {
		SpringApplication.run(SnippetApplication.class, args);
	}

}
