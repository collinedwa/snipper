package io.snipper.snippet.controller.config;

import io.snipper.snippet.model.Snippet;
import io.snipper.snippet.model.User;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;

public class ControllerConfig {

    @Bean
    public HashMap<String, User> userMap() {
        return new HashMap<>();
    }

    @Bean
    public HashMap<String, Snippet> snippetMap() {
        return new HashMap<>();
    }
}
