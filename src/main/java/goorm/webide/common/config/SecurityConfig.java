package goorm.webide.common.config;

import goorm.webide.common.jwt.JWTFilter;
import goorm.webide.common.jwt.JWTUtil;
import goorm.webide.common.jwt.LoginFilter;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                // cors 설정, Filter단 설정, controller단은 따로 해줘야함
                .cors((cors) -> cors
                        .configurationSource(new CorsConfigurationSource() {
                            @Override
                            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                                CorsConfiguration configuration = new CorsConfiguration();
                                // 허용할 front단의 주소
                                configuration.setAllowedOrigins(Collections.singletonList("*")); // 테스트 동안 모든 요청 허용
                                // 허용 메소드 : all
                                configuration.setAllowedMethods(Collections.singletonList("*"));
                                configuration.setAllowCredentials(true);
                                // 허용할 헤더
                                configuration.setAllowedHeaders(Collections.singletonList("*"));
                                // 허용할 시간
                                configuration.setMaxAge(36000L);
                                // Authorization 헤더 허용
                                configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                                return configuration;
                            }
                        }))
                // JWT 방식은 세션이 stateless 해서 csrf 를 방어할 필요 X
                .csrf((auth) -> auth.disable())
                // 스프링에서 제공하는 로그인 form disable
                .formLogin((auth) -> auth.disable())
                // HTTP 헤더에 사용자 이름과 비밀번호를 포함하여 요청을 보내는 간단한 인증 방식 disable
                .httpBasic((auth) -> auth.disable())
                // 경로별 인가
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/", "/user/register", "/user/login", "/test").permitAll()
                        .anyRequest().authenticated()
                )
                // JWTFilter 등록
                .addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                // 커스텀한 LoginFilter 를 기존자리에 적용
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil),
                        UsernamePasswordAuthenticationFilter.class)
                // 세션설정, stateless 하도록 설정**
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
