package graduation.spendiary.security.google;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoogleOAuthLoginRequest {
    private String clientId;
    private String clientSecret;
    private String code;
    private String grantType;
    private String redirectUri;
}
