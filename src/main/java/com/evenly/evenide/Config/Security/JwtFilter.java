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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException{

        String path = request.getRequestURI();
        if (path.startsWith("/auth/") || path.startsWith("/mock/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Authorization Header 에서 토큰 꺼내는 부분
        String token = resolveTokenFromHeader(request);

        if (token != null && jwtUtil.validateAccessToken(token)) {
            try {
                String userId = jwtUtil.getUserIdFromToken(token);
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
