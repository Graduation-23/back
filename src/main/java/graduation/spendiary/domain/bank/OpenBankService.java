package graduation.spendiary.domain.bank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import graduation.spendiary.security.config.OpenbankConfig;
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

import java.util.Map;

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

    public OpenBankTokenResponse requestToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
//        OpenBankTokenRequest request = OpenBankTokenRequest.builder()
//                .code(code)
//                .client_id(config.getClientId())
//                .client_secret(config.getSecret())
//                .redirect_uri(config.getRedirectUri())
//                .grant_type("authorization_code")
//                .build();
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("client_id", config.getClientId());
        body.add("client_secret", config.getSecret());
        body.add("redirect_uri", config.getRedirectUri());
        body.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
        System.out.println(requestEntity);
//        ResponseEntity<OpenBankTokenResponse> responseEntity
//                = restTemplate.postForEntity(OPEN_BANK_TOKEN_URI, requestEntity, OpenBankTokenResponse.class);
        ResponseEntity<String> stringJson = restTemplate.postForEntity(OPEN_BANK_TOKEN_URI, requestEntity, String.class);
        OpenBankTokenResponse response = (OpenBankTokenResponse) mapping(stringJson.getBody(), OpenBankTokenResponse.class);

        System.out.println(stringJson.getBody());
//        System.out.println(responseEntity.getBody());
        return response;
//        return responseEntity.getBody();
    }

    private Object mapping(String body, Class classType) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            return objectMapper.readValue(body, classType);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    public synchronized String getTokenUri(){

    }
     */
}