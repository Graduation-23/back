package graduation.spendiary.domain.bank;

import graduation.spendiary.exception.OpenBankTokenFailedException;
import graduation.spendiary.security.config.OpenBankConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Service
public class OpenBankService {
    private final OpenBankConfig config;
    private String OPEN_BANK_AUTHORIZE_URI = "https://testapi.openbanking.or.kr/oauth/2.0/authorize";
    private String OPEN_BANK_TOKEN_URI = "https://testapi.openbanking.or.kr/oauth/2.0/token";


    public String getAuthUrl(String userId) {
        return UriComponentsBuilder
                .fromUriString(OPEN_BANK_AUTHORIZE_URI)
                .queryParam("response_type", "code")
                .queryParam("client_id", config.getClientId())
                .queryParam("redirect_uri", config.getRedirectUri())
                .queryParam("scope", "login inquiry transfer")
                .queryParam("client_info", userId)
                .queryParam("state","b80BLsfigm9OokPTjy03elbJqRHOfGSY")
                .queryParam("auth_type", "0")
                .queryParam("cellphone_cert_yn", "Y")
                .queryParam("authorized_cert_yn", "Y")
                .queryParam("account_hold_auth_yn", "N")
                .queryParam("register_info", "A")
                .encode()
                .build()
                .toUriString();
    }

    public void register(String userId, String code, String state) {
        // todo: state 확인

        OpenBankTokenResponse tokenResponse = this.requestToken(code);
        // access token 헤더에 등록

        // refresh token, 사용자 번호 DB에 저장
        String accessToken = tokenResponse.getAccess_token();
        String refreshToken = tokenResponse.getRefresh_token();
        String userSeqNo = tokenResponse.getUser_seq_no();

    }

    /**
     * 금융결제원 사용자 인증 토큰을 발급받습니다.
     * @param code 사용자 인증 과정에서 받은 code
     * @return 발급 받은 토큰 정보가 포함된 Response
     * @throws OpenBankTokenFailedException 사용자 인증 토큰을 발급하는 데 실패함.
     * todo: 오류 코드 읽어서 발급 실패 원인 상세화 및 처리
     */
    private OpenBankTokenResponse requestToken(String code)
        throws OpenBankTokenFailedException
    {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("client_id", config.getClientId());
        body.add("client_secret", config.getSecret());
        body.add("redirect_uri", config.getRedirectUri());
        body.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<OpenBankTokenResponse> responseEntity
                = restTemplate.postForEntity(OPEN_BANK_TOKEN_URI, requestEntity, OpenBankTokenResponse.class);

        OpenBankTokenResponse response = responseEntity.getBody();

        assert response != null;
        if (response.getAccess_token() == null)
            throw new OpenBankTokenFailedException();

        return response;
    }
}