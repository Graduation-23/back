package graduation.spendiary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableMongoAuditing
@SpringBootApplication
public class SpendiaryApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpendiaryApplication.class, args);
    }

}
