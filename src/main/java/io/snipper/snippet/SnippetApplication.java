package io.snipper.snippet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = {"io.snipper.snippet"})
public class SnippetApplication {

	public static void main(String[] args) {
		SpringApplication.run(SnippetApplication.class, args);
	}

}
