package com.example.shopapp.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shopapp.models.Token;
import com.example.shopapp.models.User;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token, Long> {
    List<Token> findByUser(User  user);
    
    Token findByToken(String token);
    
    Token findByRefreshToken(String token);
}
