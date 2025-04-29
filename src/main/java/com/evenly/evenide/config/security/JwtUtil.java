package com.evenly.evenide.config.security;

import com.evenly.evenide.dto.JwtUserInfoDto;
import com.evenly.evenide.global.exception.CustomException;
import com.evenly.evenide.global.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {
    private final Key accessKey;
    private final Key refreshKey;
    private final long accessTokenExpireTime;
    private final long refreshTokenExpireTime;
    private final RedisTemplate<String,String> redisTemplate;

    private JwtUtil(@Value("${jwt.secret}") String accessSecretKey,
                    @Value("${jwt.refresh_secret}") String refreshSecretKey,
                    @Value("${jwt.expiration_time}") long accessTokenExpireTime,
                    RedisTemplate<String,String> redisTemplate) {
        this.accessKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(accessSecretKey));
        this.refreshKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(refreshSecretKey));
        this.accessTokenExpireTime = accessTokenExpireTime * 60 * 1000L;
        this.refreshTokenExpireTime = 3 * 24 * 60 * 60 * 1000L;
        this.redisTemplate = redisTemplate;
    }

    public String[] generateToken(JwtUserInfoDto user) {
        String accessToken = generateToken(user, accessKey, accessTokenExpireTime);
        String refreshToken = generateToken(user, refreshKey, refreshTokenExpireTime);
        return new String[]{accessToken, refreshToken};
    }

    private String generateToken(JwtUserInfoDto user, Key accessKey, long accessTokenExpireTime) {
        return Jwts.builder()
                .setSubject(user.getUserId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpireTime))
                .signWith(accessKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String renewAccessToken(String refreshToken) {
        if (!validateRefreshToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        Claims claims = parseClaims(refreshToken, refreshKey);
        String userId = claims.getSubject();
        return generateToken(new JwtUserInfoDto(userId), accessKey, accessTokenExpireTime);
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token, refreshKey);
    }

    public boolean validateAccessToken(String token){
        if (isBlacklisted(token)) {
            System.out.println("블랙리스트 처리된 토큰입니다");
            return false;
        }
        return validateToken(token, accessKey);
    }

    private boolean validateToken(String token, Key key) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    private Claims parseClaims(String token, Key key) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
    }

    private Claims parseClaims(String token) {
        return parseClaims(token, accessKey);
    }

    public String getUserIdFromToken(String token) {
        return parseClaims(token, accessKey).getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7).trim();
        }
        return null;
    }

    public String getUserIdFromRefreshToken(String token) {
        return parseClaims(token, refreshKey).getSubject();
    }

    // 컨트롤러에서 @RequestHeader(value = "Authorization") String token 방식으로 사용할 때 필요
    public String resolveToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7).trim();
        }
        return token;
    }

    public Date getExpiredTime(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(accessKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    public boolean isBlacklisted(String token) {
        String value = redisTemplate.opsForValue().get("blacklist:" + token);
        return value != null;
    }

    public void blacklistToken(String token) {
        long expiration = getExpiredTime(token).getTime() - System.currentTimeMillis();
        redisTemplate.opsForValue().set("blacklist:" + token, "logout", expiration, TimeUnit.MILLISECONDS);
    }

}
