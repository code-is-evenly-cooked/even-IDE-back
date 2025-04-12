package com.evenly.evenide.Config.Security;

import com.evenly.evenide.dto.JwtUserInfoDto;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException{

        System.out.println("🛡️ [JwtFilter] 필터 작동 시작!");
        System.out.println("🛡️ [JwtFilter] 요청 URI = " + request.getRequestURI());

        // Authorization Header 에서 토큰 꺼내는 부분
        String token = resolveTokenFromHeader(request);
        System.out.println("🛡️ [JwtFilter] token = " + token);

        if (token != null && jwtUtil.validateAccessToken(token)) {
            try {
                String userId = jwtUtil.getUserIdFromToken(token);
                System.out.println("🛡️ [JwtFilter] userId = " + userId);

                JwtUserInfoDto userInfoDto = new JwtUserInfoDto(userId);

                // 인증 객체 생성 하는 부분
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userInfoDto, null, null);

                // SecurityContext에 등록
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (JwtException e) {
                logger.warn("Invalid JWT: " + e.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }

    private String resolveTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
