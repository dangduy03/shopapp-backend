package com.example.shopapp.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.example.shopapp.filters.JwtTokenFilter;

import lombok.RequiredArgsConstructor;
import static org.springframework.http.HttpMethod.*;

@Configuration
//@EnableMethodSecurity
@EnableWebSecurity(debug = true)
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebMvc
@RequiredArgsConstructor
public class WebSecurityConfig {
	
    private final JwtTokenFilter jwtTokenFilter;
    
    @Value("${api.prefix}")
    private String apiPrefix;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)  throws Exception{
        http
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(customizer -> customizer.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> {
                    requests
                            .requestMatchers(
                                    String.format("%s/users/register", apiPrefix),
                                    String.format("%s/users/login", apiPrefix),
                                    
                                    String.format("%s/healthcheck/**", apiPrefix),
                                    String.format("%s/excel/**", apiPrefix),
                                   
                                    "/api-docs",
                                    "/api-docs/**",
                                    "/swagger-resources",
                                    "/swagger-resources/**",
                                    "/configuration/ui",
                                    "/configuration/security",
                                    "/swagger-ui/**",
                                    "/swagger-ui.html",
                                    "/webjars/swagger-ui/**",
                                    "/swagger-ui/index.html",
                                    
                                    "users/auth/social-login",
                                    "users/auth/social/callback",
                                    "actuator/health"
                            )
                            .permitAll()
                            
                            .requestMatchers(GET,
                                    String.format("%s/roles**", apiPrefix)).permitAll()

                            .requestMatchers(GET,
                                    String.format("%s/policies/**", apiPrefix)).permitAll()

                            .requestMatchers(GET,
                                    String.format("%s/categories/**", apiPrefix)).permitAll()

                            .requestMatchers(GET,
                                    String.format("%s/products/**", apiPrefix)).permitAll()

                            .requestMatchers(GET,
                                    String.format("%s/products/images/*", apiPrefix)).permitAll()

                            .requestMatchers(GET,
                                    String.format("%s/orders/**", apiPrefix)).permitAll()
                            
                            .requestMatchers(GET,
                                    String.format("%s/users/profile-images/**", apiPrefix)).permitAll()

                            .requestMatchers(GET,
                                    String.format("%s/order_details/**", apiPrefix)).permitAll()
                            
                            .requestMatchers(GET,
                                    String.format("%s/error", apiPrefix)).permitAll()
                            
                            .requestMatchers(GET,
                                    String.format ("%s/actuator/**", apiPrefix)).permitAll()
                            
                            .requestMatchers(GET,
                                    String.format ("%s/excel/**", apiPrefix)).permitAll()
                            
                            .requestMatchers("/api/v1/**").authenticated()
                            .anyRequest().permitAll();
                })
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2Login(Customizer.withDefaults())
                .oauth2ResourceServer(c -> c.opaqueToken(Customizer.withDefaults())
                );
        http.securityMatcher(String.valueOf(EndpointRequest.toAnyEndpoint()));
        return http.build();
    }
}
