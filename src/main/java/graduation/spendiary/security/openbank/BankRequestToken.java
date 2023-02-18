package graduation.spendiary.security.openbank;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BankRequestToken {
    private String code;
    private String client_id;
    private String client_secret;
    private String redirect_uri;
    private String grant_type; //고정값: authorization_code
}