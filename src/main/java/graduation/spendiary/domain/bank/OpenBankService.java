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

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OpenBankService {
    private final OpenbankConfig config;
    private String OPEN_BANK_BASIC_URI = "https://testapi.openbanking.or.kr/oauth/2.0/authorize?";


    public synchronized String getAuthUrl(String userId) {
        Map<String, Object> queries = new HashMap<>();
        queries.put("response_type", "code");
        queries.put("client_id", config.getClientId());
        queries.put("redirect_url", config.getRedirectUri());
        queries.put("scope", "login inquiry transfer".replaceAll(" ", "%20"));
        queries.put("client_info", userId);
        queries.put("state","b80BLsfigm9OokPTjy03elbJqRHOfGSY");
        queries.put("auth_type", "0");
        queries.put("cellphone_cert_yn", "Y");
        queries.put("authorized_cert_yn", "Y");
        queries.put("account_hold_auth_yn", "N");
        queries.put("register_info", "A");

        return OPEN_BANK_BASIC_URI
                + queries.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
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
        ResponseEntity<String> stringJson = restTemplate.postForEntity(OPEN_BANK_BASIC_URI, requestEntity, String.class);
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