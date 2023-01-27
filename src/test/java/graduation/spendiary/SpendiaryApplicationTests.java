package graduation.spendiary;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import graduation.spendiary.security.jwt.Token;
import graduation.spendiary.util.responseFormat.ResponseFormat;
import graduation.spendiary.util.responseFormat.ServiceType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpendiaryApplicationTests {

    // @Test
    void responseFormatTest() throws JsonProcessingException {
        ResponseFormat<String> data = ResponseFormat.from(
                ServiceType.AUTH,
                "/test",
                true,
                "Hello Test!",
                "testaccesstoken",
                "testrefreshtoken"
        );

        ObjectMapper mapper = new ObjectMapper();

        System.out.println(mapper.writeValueAsString(data));
    }

}
