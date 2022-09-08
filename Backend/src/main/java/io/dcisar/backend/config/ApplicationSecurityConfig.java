package io.dcisar.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig {

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeRequests(auth -> {
                    auth.antMatchers("/api/**").permitAll();
                    auth.antMatchers("/api/**/rating/create").authenticated();//.hasRole("USER")
                    auth.antMatchers("/admin").authenticated();//.hasRole("ADMIN");
                })
                .httpBasic(Customizer.withDefaults())
                .build();
    }
}
