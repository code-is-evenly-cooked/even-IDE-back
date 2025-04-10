package com.evenly.evenide.Config.Security;

import com.evenly.evenide.dto.JwtUserInfoDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private final Key accessKey;
    private final Key refreshKey;
    private final long accessTokenExpireTime;
    private final long refreshTokenExpireTime;

    private JwtUtil(@Value("${jwt.secret}") String accessSecretKey,
                    @Value("${jwt.refresh_secret}") String refreshSecretKey,
                    @Value("${jwt.expiration_time}") long accessTokenExpireTime) {
        this.accessKey = Keys.hmacShaKeyFor(accessSecretKey.getBytes());
        this.refreshKey = Keys.hmacShaKeyFor(refreshSecretKey.getBytes());
        this.accessTokenExpireTime = accessTokenExpireTime * 60 * 1000L;
        this.refreshTokenExpireTime = 3 * 24 * 60 * 60 * 1000L;
    }

    public String[] generateToken(JwtUserInfoDto user) {
        String accessToken = generateToken(user, accessKey, accessTokenExpireTime);
        String refreshToken = generateToken(user, refreshKey, refreshTokenExpireTime);
        return new String[]{accessToken, refreshToken};
    }

    private String generateToken(JwtUserInfoDto user, Key accessKey, long accessTokenExpireTime) {
        return Jwts.builder()
                .setSubject(user.getUserId())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpireTime))
                .signWith(accessKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String renewAccessToken(String refreshToken) {
        if (!vaildateRefreshToken(refreshToken)) {
            throw new JwtException("Refresh token이 만료되었습니다. 다시 로그인하세요.");
        }
        Claims claims = parseClaims(refreshToken, refreshKey);
        String userId = claims.getSubject();
        String role = claims.get("role", String.class);
        return generateToken(new JwtUserInfoDto(userId, role), accessKey, accessTokenExpireTime);
    }

    private boolean vaildateRefreshToken(String token) {
        return vaildateToken(token, refreshKey);
    }

    private boolean vailldateAccessToken(String token) {
        return vaildateToken(token, accessKey);
    }

    private boolean vaildateToken(String token, Key key) {
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

    private String getUserIdFromToken(String token) {
        return parseClaims(token, accessKey).getSubject();
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7).trim();
        }
        return null;
    }

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
}
