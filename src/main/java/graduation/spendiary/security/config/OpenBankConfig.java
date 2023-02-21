package graduation.spendiary.security.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class OpenBankConfig {

    @Value("${openbank.redirect-uri}")
    private String redirectUri;
    @Value("${openbank.tran-id}")
    private String tranId;
    @Value("${openbank.client-id}")
    private String clientId;
    @Value("${openbank.secret}")
    private String secret;
}
