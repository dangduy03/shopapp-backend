package com.example.shopapp.services.token;

import org.springframework.stereotype.Service;

import com.example.shopapp.models.Token;
import com.example.shopapp.models.User;

@Service
public interface ITokenService {
	
    Token addToken(User user, String token, boolean isMobileDevice);
    
    Token refreshToken(String refreshToken, User user) throws Exception;
}

