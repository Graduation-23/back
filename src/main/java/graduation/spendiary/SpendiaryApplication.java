package graduation.spendiary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableAutoConfiguration
@EnableMongoAuditing
@SpringBootApplication
public class SpendiaryApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpendiaryApplication.class, args);
    }

}
