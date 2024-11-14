package com.sparta.doguin.config;

import com.sparta.doguin.domain.user.enums.UserRole;
import com.sparta.doguin.security.JwtSecurityFilter;
import lombok.RequiredArgsConstructor;
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
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtSecurityFilter, SecurityContextHolderAwareRequestFilter.class)
                .formLogin(AbstractHttpConfigurer::disable)
                .anonymous(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/signup", "/api/v1/auth/signin").permitAll()
                        .requestMatchers("/auth/oauth2/authorize/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/api-docs/**").permitAll()
                        .requestMatchers("/test/**").permitAll()
                        .requestMatchers("/health").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/discussions/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/discussions/search").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/boards/*/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/boards/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/boards/*/search").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/question/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/question/search").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/question/rank").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/outsourcings").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/outsourcings/search").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/portfolios/my").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/v1/portfolios/other").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/portfolios/{id}").permitAll()


                        .requestMatchers(HttpMethod.POST, "/boards/events").hasAuthority(UserRole.Authority.ADMIN)
                        .requestMatchers(HttpMethod.POST, "/boards/notices").hasAuthority(UserRole.Authority.ADMIN)
                        .requestMatchers(HttpMethod.POST, "/boards/inquiries").hasAuthority(UserRole.Authority.USER)
                        .requestMatchers(HttpMethod.PUT, "/reports/*/accept").hasAuthority(UserRole.Authority.ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/reports/*/inject").hasAuthority(UserRole.Authority.ADMIN)
                        .requestMatchers(HttpMethod.GET, "/reports/total/*").hasAuthority(UserRole.Authority.ADMIN)

                        .anyRequest().authenticated()
                )
                .build();
    }

}
