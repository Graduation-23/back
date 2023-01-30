package graduation.spendiary.security.google;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class GoogleConfig {

    @Value("${google.redirectUri}")
    private String redirectUri;
    @Value("${google.scope}")
    private String scope;
    @Value("${google.clientId}")
    private String clientId;
    @Value("${google.clientSecret}")
    private String clientSecret;
}
