package graduation.spendiary.security.openbank;

import lombok.Data;

@Data
public class BankResponseToken {
    private String access_token; //오픈뱅킹에서 발행된 Access Token
    private String token_type; //Access Token 유형
    private int expires_in; //Access Token 만료 기간(초)
    private String refresh_token; //갱신 시 사용하는 Refresh Token
    private String scope; //Access Token 권한 범위
    private String user_seq_no; //사용자일련번호
}