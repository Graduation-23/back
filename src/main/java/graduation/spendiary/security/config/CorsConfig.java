package graduation.spendiary.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**") // 프로그램에서 제공하는 URL
                .allowedOrigins("http://localhost:5173", "http://localhost:8080") // 청을 허용할 출처를 명시, 전체 허용 (가능하다면 목록을 작성한다.
                .allowedHeaders("*") // 어떤 헤더들을 허용할 것인지
                .allowedMethods("*") // 어떤 메서드를 허용할 것인지 (GET, POST...)
                .allowCredentials(true);
    }
}
