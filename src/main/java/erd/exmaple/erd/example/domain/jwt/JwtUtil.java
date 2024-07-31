package erd.exmaple.erd.example.domain.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    // 비밀 키를 애플리케이션 설정 파일에서 읽어옵니다.
    @Value("${jwt.secret}")
    private String secret;

    private Key getSigningKey() {
        logger.debug("Using secret key: {}", secret); // 시크릿 키 로그 추가
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String extractUsername(String token) {
        // JWT에서 사용자 이름을 추출합니다.
        try {
            String username = extractClaim(token, Claims::getSubject);
            logger.info("Extracted username: {}", username);
            return username;
        } catch (Exception e) {
            logger.error("Error extracting username from token: ", e);
            return null;
        }
    }

    public Date extractExpiration(String token) {
        // JWT에서 만료 시간을 추출합니다.
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        // JWT에서 모든 클레임을 추출합니다.
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        // JWT가 만료되었는지 확인합니다.
        return extractExpiration(token).before(new Date());
    }
    // UserDetails 객체를 인수로 받아 JWT 토큰 생성
    public String generateToken(UserDetails userDetails) {
        // JWT를 생성합니다.
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }
    // 사용자 이름을 인수로 받아 JWT 토큰 생성
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        // JWT를 생성하는 메서드입니다.
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 만료 시간 설정 (10시간)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        // JWT가 유효한지 검증합니다.
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}