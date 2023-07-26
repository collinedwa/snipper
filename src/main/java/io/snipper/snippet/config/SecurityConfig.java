package io.snipper.snippet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public OAuth2LoginConfigurer<HttpSecurity> oAuth2LoginConfigurer(HttpSecurity http) throws Exception {
        return http.authorizeRequests()
                .requestMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and().oauth2Login();
    }
}
