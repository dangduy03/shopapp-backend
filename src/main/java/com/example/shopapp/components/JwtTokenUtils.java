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

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {

	@Value("${jwt.expiration-time}")
	private int expiration;

	@Value("${jwt.expiration-refresh-token}")
	private int expirationRefreshToken;

	@Value("${jwt.secret-key}")
	private String secretKey;

	private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtils.class);

	private final TokenRepository tokenRepository;

	public String generateToken(User user) throws Exception {
		Map<String, Object> claims = new HashMap<>();
		String subject = getSubject(user);
		claims.put("subject", subject);
		claims.put("userId", user.getId());
		try {
			String token = Jwts.builder().setClaims(claims)
					.setSubject(subject).setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))
					.signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
			return token;
		} catch (Exception e) {
			throw new InvalidParamException("Cannot create jwt token, error: " + e.getMessage());
		}
	}

	private static String getSubject(User user) {
		String subject = user.getPhoneNumber();
		if (subject == null || subject.isBlank()) {
			subject = user.getEmail();
		}
		return subject;
	}

	private SecretKey getSignInKey() {
		byte[] bytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(bytes);
	}

	private String generateSecretKey() {
		SecureRandom random = new SecureRandom();
		byte[] keyBytes = new byte[32];
		random.nextBytes(keyBytes);
		String secretKey = Encoders.BASE64.encode(keyBytes);
		return secretKey;
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSignInKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = this.extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	// check expiration
	public boolean isTokenExpired(String token) {
		Date expirationDate = this.extractClaim(token, Claims::getExpiration);
		return expirationDate.before(new Date());
	}

	public String getSubject(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public boolean validateToken(String token, User userDetails) {
		try {
			String subject = extractClaim(token, Claims::getSubject);
			Token existingToken = tokenRepository.findByToken(token);
			if (existingToken == null || existingToken.isRevoked() == true || !userDetails.isActive()) {
				return false;
			}
			return (subject.equals(userDetails.getUsername())) && !isTokenExpired(token);

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
