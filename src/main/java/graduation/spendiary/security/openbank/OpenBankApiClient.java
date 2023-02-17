package graduation.spendiary.security.openbank;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class OpenBankApiClient {
    private final HttpHeaders httpHeaders;
    private final RestTemplate restTemplate;
    private final OpenbankConfig config;

    private String baseUrl = "https://openapi.openbanking.or.kr/oauth/2.0";

    /**
     * 토큰 발급 요청
     */
    public BankResponseToken requestToken(BankRequestToken bankRequestToken) {
        //http Header 오브젝트 생성
        httpHeaders.add("Content-Type" ,"application/x-www-form-urlencoded;charset=UTF-8");
        //http Body 오브젝트 생성
        bankRequestToken.setBankRequestToken(config.getClientId(), config.getSecret(), config.getRedirectUri(), "authorization_code");
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("code", bankRequestToken.getCode());
        parameters.add("client_id", bankRequestToken.getClient_id());
        parameters.add("client_secret", bankRequestToken.getClient_secret());
        parameters.add("grant_type", bankRequestToken.getGrant_type());
        //httpHeader&httpBody 모으기
        HttpEntity<MultiValueMap<String, String>> param = new HttpEntity<>(parameters, httpHeaders);
        return restTemplate.exchange("https://openapi.openbanking.or.kr/oauth/2.0/token", HttpMethod.POST, param, BankResponseToken.class).getBody();
    }
}