package io.snipper.snippet.controller.config;

import io.snipper.snippet.service.AuthorizationService;
import io.snipper.snippet.service.EncryptionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.cors().disable()
                .csrf().disable()
                .authorizeHttpRequests().requestMatchers("/**").permitAll().and()
                .build();
    }

    @Bean
    public AuthorizationService authorizationService() throws Exception {
        return new AuthorizationService(encryptionService());
    }

    @Bean
    public EncryptionService encryptionService() throws Exception {
        return new EncryptionService();
    }
}
