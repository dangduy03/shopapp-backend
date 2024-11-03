package com.example.shopapp.configurations;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.example.shopapp.filters.JwtTokenFilter;
import com.example.shopapp.models.Role;

import lombok.RequiredArgsConstructor;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity(debug = true)
@EnableWebMvc
@RequiredArgsConstructor
//public class WebSecurityConfig {
//	
//	private final JwtTokenFilter jwtTokenFilter;
//	
//	@Value("${api.prefix}")
//	private String apiPrefix;
//	
//	@Bean
//	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//		http
//			.cors()
//			.and()
//			.csrf(AbstractHttpConfigurer::disable)
//			.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
//			.authorizeHttpRequests(requests -> {
//				requests
//						.requestMatchers(
//								String.format("%s/users/register", apiPrefix),
//								String.format("%s/users/login", apiPrefix),
//								String.format("%s/products/upload-multiple/**", apiPrefix)
//						)
//						.permitAll()
//						
//						.requestMatchers(HttpMethod.GET,
//                                String.format("%s/roles**", apiPrefix)).permitAll()
//						
//						
//						.requestMatchers(HttpMethod.GET, 
//								String.format("%s/categories/**", apiPrefix)).permitAll()
//						.requestMatchers(HttpMethod.POST, 
//								String.format("%s/categories/**", apiPrefix)).hasAnyRole(Role.ADMIN)
//						.requestMatchers(HttpMethod.PUT, 
//								String.format("%s/categories/**", apiPrefix)).hasAnyRole(Role.ADMIN)
//						.requestMatchers(HttpMethod.DELETE, 
//								String.format("%s/categories/**", apiPrefix)).hasAnyRole(Role.ADMIN)
//						
//						
//						.requestMatchers(HttpMethod.GET, 
//								String.format("%s/products/images/*", apiPrefix)).permitAll()
//						.requestMatchers(HttpMethod.GET, 
//								String.format("%s/products/**", apiPrefix)).permitAll()
//						.requestMatchers(HttpMethod.POST, 
//								String.format("%s/products/**", apiPrefix)).hasAnyRole(Role.ADMIN)
//						.requestMatchers(HttpMethod.PUT, 
//								String.format("%s/products/**", apiPrefix)).hasAnyRole(Role.ADMIN)
//						.requestMatchers(HttpMethod.DELETE, 
//								String.format("%s/products/**", apiPrefix)).hasAnyRole(Role.ADMIN)
//						
//						
//						
//						.requestMatchers(HttpMethod.GET, 
//								String.format("%s/orders/**", apiPrefix)).permitAll()
//						.requestMatchers(HttpMethod.POST, 
//								String.format("%s/orders/**", apiPrefix)).hasAnyRole(Role.USER)
//						.requestMatchers(HttpMethod.PUT, 
//								String.format("%s/orders/**", apiPrefix)).hasRole(Role.ADMIN)
//						.requestMatchers(HttpMethod.DELETE, 
//								String.format("%s/orders/**", apiPrefix)).hasRole(Role.ADMIN)
//						
//						
//						
//						.requestMatchers(HttpMethod.GET, 
//								String.format("%s/order_details/**", apiPrefix)).permitAll()
//						.requestMatchers(HttpMethod.POST, 
//								String.format("%s/order_details/**", apiPrefix)).hasAnyRole(Role.USER)
//						.requestMatchers(HttpMethod.PUT, 
//								String.format("%s/order_details/**", apiPrefix)).hasRole(Role.ADMIN)
//						.requestMatchers(HttpMethod.DELETE, 
//								String.format("%s/order_details/**", apiPrefix)).hasRole(Role.ADMIN)
//						
//						
//						.anyRequest()
//						.authenticated();
//				
//				});
//			
////				.csrf(AbstractHttpConfigurer::disable);
////
////		http.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
////			@Override
////			public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer)  {
////				CorsConfiguration configuration = new CorsConfiguration();
////				configuration.setAllowedOrigins(List.of("*"));
//////				configuration.setAllowedOrigins(List.of("http://localhost:4200"));
////				configuration.setAllowedMethods(Arrays.asList("GET", "POST","PUT", "PATCH", "DELETE", "OPTIONS"));;
////				configuration.setAllowedHeaders(Arrays.asList("authorization","content-type", "x-auth-token"));
////				configuration.setAllowedHeaders(List.of("x-auth-token"));
////				UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
////				source.registerCorsConfiguration("/**", configuration);
////				httpSecurityCorsConfigurer.configurationSource(source);
////			}
////		});
//		
//		return http.build();
//	}
//	
//	@Bean
//	CorsConfigurationSource corsConfigurationSource() {
//	    CorsConfiguration configuration = new CorsConfiguration();
////	    configuration.setAllowedOrigins(Arrays.asList("*"));
//	    configuration.setAllowedMethods(Arrays.asList("*"));
//	    configuration.setAllowedHeaders(Arrays.asList("*"));
//		configuration.setAllowedOrigins(List.of("http://localhost:4200"));
////	    configuration.setAllowedMethods(Arrays.asList("GET", "POST","PUT", "PATCH", "DELETE", "OPTIONS"));;
////		configuration.setAllowedHeaders(Arrays.asList("authorization","content-type", "x-auth-token"));
////		configuration.setAllowedHeaders(List.of("x-auth-token"));
//	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//	    source.registerCorsConfiguration("/**", configuration);
//	    return source;
//	}
//
//}


public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    
    @Value("${api.prefix}")
    private String apiPrefix;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)  throws Exception{
        http
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                //.cors(Customizer.withDefaults())
                .exceptionHandling(customizer -> customizer.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> {
                    requests
                            .requestMatchers(
                                    String.format("%s/users/register", apiPrefix),
                                    String.format("%s/users/login", apiPrefix),
                                    //healthcheck
                                    String.format("%s/healthcheck/**", apiPrefix),
                                    //swagger
                                    //"/v3/api-docs",
                                    //"/v3/api-docs/**",
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
                                    //Google login
                                    "users/auth/social-login",
                                    "users/auth/social/callback"

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
                                    String.format("%s/users/profile-images/**", apiPrefix))
                            .permitAll()

                            .requestMatchers(GET,
                                    String.format("%s/order_details/**", apiPrefix)).permitAll()

                            .anyRequest()
                            .authenticated();
                            //.anyRequest().permitAll();
                })
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2Login(Customizer.withDefaults())
                .oauth2ResourceServer(c -> c.opaqueToken(Customizer.withDefaults())
                );
        http.securityMatcher(String.valueOf(EndpointRequest.toAnyEndpoint()));
        return http.build();
    }
}
