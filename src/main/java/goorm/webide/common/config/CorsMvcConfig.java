package goorm.webide.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        // allowedOrigins에서 오는 모든 경로(/**)에 대해서 허용
        corsRegistry.addMapping("/**")
                .allowedOrigins("*"); // 테스트 동안만 모든요청"*"허용
    }
}
