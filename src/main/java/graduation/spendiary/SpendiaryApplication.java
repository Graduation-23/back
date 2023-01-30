package graduation.spendiary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableMongoAuditing
@SpringBootApplication
@EnableSwagger2
public class SpendiaryApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SpendiaryApplication.class, args);
    }

}
