package graduation.spendiary.domain.bank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import graduation.spendiary.security.openbank.BankRequestToken;
import graduation.spendiary.security.openbank.BankResponseToken;
import graduation.spendiary.security.openbank.OpenbankConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Service
public class OpenBankService {
    private final OpenbankConfig config;
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

    public BankResponseToken requestToken(String code) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        BankRequestToken request = BankRequestToken.builder()
                .code(code)
                .client_id(config.getClientId())
                .client_secret(config.getSecret())
                .redirect_uri(config.getRedirectUri())
                .grant_type("authorization_code")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<BankRequestToken> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<String> stringJson = restTemplate.postForEntity(OPEN_BANK_TOKEN_URI, requestEntity, String.class);
        BankResponseToken response = (BankResponseToken) mapping(stringJson.getBody(), BankResponseToken.class);

        if(response != null) return response;
        else throw new Exception("Failed");
    }

    private Object mapping(String body, Class classType) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper.readValue(body, classType);
    }

    /*
    public synchronized String getTokenUri(){

    }
     */
}