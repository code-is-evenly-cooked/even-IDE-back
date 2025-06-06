package com.evenly.evenide.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final JwtUtil jwtUtil;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable) // JWT 사용으로 필요 없음
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/mock/**").permitAll() // mock api
                        .requestMatchers("/auth/**").permitAll() // 로그인, 회원가입 인증 없이
                        .requestMatchers(HttpMethod.GET, "/projects/*", "/projects/*/files/*").permitAll() // 프로젝트, 파일 단건 조회 인증 없이
                        .requestMatchers(HttpMethod.PATCH, "/projects/*/files/*/code").permitAll() // 코드 수정 인증 없이
                        .requestMatchers(HttpMethod.POST, "/code/execute").permitAll()// 코드 실행 인증 없이
                        .requestMatchers(
                                "/chat-test.html",
                                "/chat-and-editor.html",
                                "/chat-diff-and-cursor.html",
                                "/ws/**",
                                "/chat/join",
                                "/chat/history",
                                "/topic/**",
                                "/app/**").permitAll() // 웹소켓
                        .anyRequest().authenticated() // 그외 모든 요청은 인증 필요
                )
                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOrigin("https://even-ide.vercel.app");
        configuration.addAllowedOrigin("https://together-cute-fly.ngrok-free.app");
        configuration.addAllowedMethod("GET");
        configuration.addAllowedMethod("POST");
        configuration.addAllowedMethod("PUT");
        configuration.addAllowedMethod("PATCH");
        configuration.addAllowedMethod("DELETE");
        configuration.addAllowedMethod("OPTIONS");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
