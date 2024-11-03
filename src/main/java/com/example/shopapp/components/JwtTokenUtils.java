package com.example.shopapp.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.shopapp.exceptions.InvalidParamException;
import com.example.shopapp.models.Token;
import com.example.shopapp.models.User;
import com.example.shopapp.repositorys.TokenRepository;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;

import io.jsonwebtoken.Jwts.*;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {
	
//	@Value("${jwt.expiration-time}")
//	private long expiration;
////	private int expiration; // save to enviroment varibale
//
//	
//	@Value("${jwt.secret-key}")
//	private String secretKey;
//	 
//    public String extractPhoneNumber(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//
//    public String generateToken(User user) {
//        return generateToken(new HashMap<>(), user);
//    }
//
//    public String generateToken(Map<String, Object> extraClaims, User user) {
//        return buildToken(extraClaims, user, expiration);
//    }
//
//    public long getExpirationTime() {
//        return expiration;
//    }
//
//    private String buildToken(
//            Map<String, Object> extraClaims,
//            User user,
//            long expiration
//    ) {
//        return Jwts
//                .builder()
//                .setClaims(extraClaims)// how to extract claims from to?
//                .setSubject(user.getPhoneNumber())
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L)) // thời gian đổi ra mili giây
//                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    public boolean isTokenValid(String token, UserDetails userDetails) {
//        final String phoneNumber = extractPhoneNumber(token);
//        return (phoneNumber.equals(userDetails.getUsername())) && !isTokenExpired(token);
//    }//kt co bị trùng giữa phone và username
//
//    private boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());//muốn trả JWT token
//    }
//
//    private Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//
//    private Claims extractAllClaims(String token) {
//        return Jwts
//                .parserBuilder()
//                .setSigningKey(getSignInKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    private Key getSignInKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//    
//    private String generateSecretKey() {
//        SecureRandom random = new SecureRandom();
//        byte[] keyBytes = new byte[32]; // 256-bit key
//        random.nextBytes(keyBytes);
//        String secretKey = Encoders.BASE64.encode(keyBytes);
//        return secretKey;
//    }
	
	
    @Value("${jwt.expiration-time}")
    private int expiration; //save to an environment variable

    @Value("${jwt.expiration-refresh-token}")
    private int expirationRefreshToken;

    @Value("${jwt.secret-key}")
    private String secretKey;
    
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtils.class);
    
    private final TokenRepository tokenRepository;
    
    public String generateToken(User user) throws Exception{
        //properties => claims
        Map<String, Object> claims = new HashMap<>();
        // Add subject identifier (phone number or email)
        String subject = getSubject(user);
        claims.put("subject", subject);
        // Add user ID
        claims.put("userId", user.getId());
        try {
            String token = Jwts.builder()
                    .setClaims(claims) //how to extract claims from this ?
                    .setSubject(subject)
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
            return token;
        }catch (Exception e) {
            //you can "inject" Logger, instead System.out.println
            throw new InvalidParamException("Cannot create jwt token, error: "+e.getMessage());
            //return null;
        }
    }
    private static String getSubject(User user) {
        // Determine subject identifier (phone number or email)
        String subject = user.getPhoneNumber();
        if (subject == null || subject.isBlank()) {
            // If phone number is null or blank, use email as subject
            subject = user.getEmail();
        }
        return subject;
    }
    private SecretKey getSignInKey() {
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        //Keys.hmacShaKeyFor(Decoders.BASE64.decode("TaqlmGv1iEDMRiFp/pHuID1+T84IABfuA0xXh4GhiUI="));
        return Keys.hmacShaKeyFor(bytes);
    }


    private String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[32]; // 256-bit key
        random.nextBytes(keyBytes);
        String secretKey = Encoders.BASE64.encode(keyBytes);
        return secretKey;
    }
//    private Claims extractAllClaims(String token) {
//        return Jwts.parser()  // Khởi tạo JwtParserBuilder
//                .verifyWith(getSignInKey())  // Sử dụng verifyWith() để thiết lập signing key
//                .build()  // Xây dựng JwtParser
//                .parseSignedClaims(token)  // Phân tích token đã ký
//                .getPayload();  // Lấy phần body của JWT, chứa claims
//    }
    
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()  // Khởi tạo JwtParserBuilder
                .setSigningKey(getSignInKey())  // Sử dụng setSigningKey() để thiết lập signing key
                .build()  // Xây dựng JwtParser
                .parseClaimsJws(token)  // Phân tích token đã ký
                .getBody();  // Lấy phần body của JWT, chứa claims
    }




    public  <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    //check expiration
    public boolean isTokenExpired(String token) {
        Date expirationDate = this.extractClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }
    
    public String getSubject(String token) {
        return  extractClaim(token, Claims::getSubject);
    }
    
    public boolean validateToken(String token, User userDetails) {
        try {
            String subject = extractClaim(token, Claims::getSubject);
            //subject is phoneNumber or email
            Token existingToken = tokenRepository.findByToken(token);
            if(existingToken == null ||
                    existingToken.isRevoked() == true ||
                    !userDetails.isActive()
            ) {
                return false;
            }
            return (subject.equals(userDetails.getUsername()))
                    && !isTokenExpired(token);
            
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
            
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
            
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
            
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
