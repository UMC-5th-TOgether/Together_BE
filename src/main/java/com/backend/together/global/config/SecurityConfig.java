package com.backend.together.global.config;

import com.backend.together.global.security.JwtFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) {
        try {
            http.csrf(AbstractHttpConfigurer::disable)
                    .httpBasic(AbstractHttpConfigurer::disable)
                    .cors(Customizer.withDefaults())
                    .sessionManagement((sessionManagement) ->
                            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    )

                    .authorizeHttpRequests((authorizeRequests) ->
                            authorizeRequests.requestMatchers("/", "/api/auth/**", "/api/test").permitAll()
                                    .requestMatchers("/ws/chat/**").permitAll()
                                    .anyRequest().authenticated()
                    );

            http.addFilterAfter(
                    jwtFilter,
                    CorsFilter.class
            );

            return http.build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

