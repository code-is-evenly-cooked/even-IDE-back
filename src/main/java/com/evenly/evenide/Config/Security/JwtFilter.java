package com.evenly.evenide.Config.Security;

import com.evenly.evenide.dto.JwtUserInfoDto;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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

        String token = jwtUtil.resolveToken(request);

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

}
