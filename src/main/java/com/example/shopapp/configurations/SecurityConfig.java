package com.example.shopapp.configurations;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.shopapp.models.User;
import com.example.shopapp.repositorys.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
	private final UserRepository userRepository;
	
	private final WebClient userInfoClient;
	
	 // user's deatail object
	@Bean 
	public UserDetailsService userDetailsService() { 
		
		return subject -> {
            // Attempt to find user by phone number
            Optional<User> userByPhoneNumber = userRepository.findByPhoneNumber(subject);
            if (userByPhoneNumber.isPresent()) {
                return userByPhoneNumber.get(); // Return UserDetails if found
            }

            // If user not found by phone number, attempt to find by email
            Optional<User> userByEmail = userRepository.findByEmail(subject);
            if (userByEmail.isPresent()) {
                return userByEmail.get(); // Return UserDetails if found
            }

            // If user not found by either phone number or email, throw UsernameNotFoundException
            throw new UsernameNotFoundException("User not found with subject: " + subject);
        };
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager( 
			AuthenticationConfiguration config
	) throws Exception {
		return config.getAuthenticationManager();
	}
	
	@Bean
    public OpaqueTokenIntrospector introspector() {
        return new GoogleOpaqueTokenIntrospector(userInfoClient);
    }


}