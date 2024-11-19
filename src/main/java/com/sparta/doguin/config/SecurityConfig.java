package com.sparta.doguin.config;

import com.sparta.doguin.domain.user.enums.UserRole;
import com.sparta.doguin.security.JwtSecurityFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final JwtSecurityFilter jwtSecurityFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정 활성화
                .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtSecurityFilter, SecurityContextHolderAwareRequestFilter.class) // JWT 필터
                .formLogin(AbstractHttpConfigurer::disable) // Form Login 비활성화
                .httpBasic(AbstractHttpConfigurer::disable) // HTTP Basic 인증 비활성화
                .logout(AbstractHttpConfigurer::disable) // Logout 비활성화
                .authorizeHttpRequests(auth -> auth
                        // OPTIONS 메서드는 모든 요청 허용
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 인증이 필요 없는 공개 엔드포인트
                        .requestMatchers(
                                "/ws/**",
                                "/api/v1/auth/signup",
                                "/api/v1/auth/signin",
                                "/api/v1/auth/oauth2/authorize/**",
                                "/swagger-ui/**",
                                "/api-docs/**",
                                "/test/**",
                                "/health",
                                "/actuator/**",
                                "/error"
                        ).permitAll()
                        // GET 요청에 대한 공개 엔드포인트
                        .requestMatchers(HttpMethod.GET, "/api/v1/**").permitAll()

                        // 인증이 필요한 특정 엔드포인트
                        .requestMatchers("/api/v1/chat/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/portfolios/my").authenticated()

                        // 관리자 전용 엔드포인트
                        .requestMatchers(HttpMethod.POST, "/boards/events").hasAuthority(UserRole.Authority.ADMIN)
                        .requestMatchers(HttpMethod.POST, "/boards/notices").hasAuthority(UserRole.Authority.ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/reports/*/accept").hasAuthority(UserRole.Authority.ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/reports/*/inject").hasAuthority(UserRole.Authority.ADMIN)
                        .requestMatchers(HttpMethod.GET, "/reports/total/*").hasAuthority(UserRole.Authority.ADMIN)

                        // 인증이 필요한 모든 나머지 요청
                        .anyRequest().authenticated()
                )
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:3001",
                "http://doguin-alb-1242367005.ap-northeast-2.elb.amazonaws.com",
                "https://doguin-f.vercel.app"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Content-Type", "Authorization", "Accept"));
        config.setAllowCredentials(true); // 인증 정보 허용
        config.setMaxAge(3600L); // Preflight 요청 캐싱 시간
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:3001",
                "http://doguin-alb-1242367005.ap-northeast-2.elb.amazonaws.com",
                "https://doguin-f.vercel.app"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Content-Type", "Authorization", "Accept"));
        config.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", config);

        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(0); // SecurityFilterChain보다 우선 적용
        return bean;
    }
}
