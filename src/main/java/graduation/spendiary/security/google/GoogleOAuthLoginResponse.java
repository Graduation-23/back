package graduation.spendiary.security.google;

import lombok.Data;

import java.util.List;

@Data
public class GoogleOAuthLoginResponse {
    private String accessToken;
    private int expiresIn;
    private String refreshToken;
    private String scope;
    private String tokenType;
    private String idToken;
}
