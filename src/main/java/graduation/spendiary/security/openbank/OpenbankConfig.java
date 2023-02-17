package graduation.spendiary.security.openbank;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class OpenbankConfig {

    @Value("${openbank.redirectUri}")
    private String redirectUri;
    @Value("${openbank.tranId}")
    private String tranId;
    @Value("${openbank.clientId}")
    private String clientId;
    @Value("${openbank.secret}")
    private String secret;
}
