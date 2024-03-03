package com.jwt.jwt.authentication.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityFilterChainConfig {

    @Autowired
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private final JWTAuthenticationFilter jwtAuthenticationFilter;

    public SecurityFilterChainConfig(CustomAuthenticationEntryPoint authenticationEntryPoint, JWTAuthenticationFilter jwtAuthenticationFilter) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        //Disable cors
        //httpSecurity.cors(corsConfig->corsConfig.configurationSource(getConfigurationSource()));

        httpSecurity.cors(cors->cors.disable());

        //Disable CSRF
        httpSecurity.csrf(csrf->csrf.disable());

        //Filter Request
        httpSecurity.authorizeHttpRequests(
                requestMatcher->
                        requestMatcher.requestMatchers("/api/auth/login/**").permitAll()
                                .requestMatchers("/api/auth/sign-up/**").permitAll()
                                .requestMatchers("/api/auth/verify-token/**").permitAll()
                                .anyRequest().authenticated());

    // Now agar koi exception aata hai for authentication to apan log ye exception handler
        httpSecurity.exceptionHandling(
                exceptionConfig->exceptionConfig.authenticationEntryPoint(authenticationEntryPoint));

    //Now we dont want to store anything so we will put session policy stateless
        httpSecurity.sessionManagement(
                sessionConfig->sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    //Now we will authenticate JWT Authenticate token with Authentication Filter
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

//    private static CorsConfigurationSource getConfigurationSource(){
//        var corsConfiguration = new CorsConfiguration();
//        corsConfiguration.setAllowedMethods(List.of("*"));
//        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000/", "http://localhost:8080"));
//        corsConfiguration.setAllowedHeaders(List.of("Content-Type"));
//
//        var source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", corsConfiguration);
//
//        return  source;
//    }
}
