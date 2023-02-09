package graduation.spendiary.security.google;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GoogleOAuthHelper {

    @Autowired
    private GoogleConfig config;

    private final String GOOGLE_AUTH_BASE_URI = "https://accounts.google.com/o/oauth2/v2/auth?";
    private final String GOOGLE_LOGIN_BASE_URI = "https://oauth2.googleapis.com/token";
    private final String GOOGLE_PROFILE_BASE_URI = "https://oauth2.googleapis.com/tokeninfo?id_token=";

    private String authUri;

    public GoogleUser requestProfile(String accessToken, String idToken) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        ResponseEntity<String> stringifyJson = restTemplate.postForEntity(
                GOOGLE_PROFILE_BASE_URI + idToken,
                new HttpEntity(headers),
                String.class
        );

        GoogleUser user = (GoogleUser) mapping(stringifyJson.getBody(), GoogleUser.class);

        if(user != null) {
            return user;
        }
        else {
            throw new Exception("Google OAuth Login failed!");
        }
    }

    public GoogleOAuthLoginResponse requestOAuthLogin(String code) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        GoogleOAuthLoginRequest request = GoogleOAuthLoginRequest.builder()
                .clientId(config.getClientId())
                .clientSecret(config.getClientSecret())
                .code(code)
                .grantType("authorization_code")
                .redirectUri(config.getRedirectUri())
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<GoogleOAuthLoginRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<String> stringifyJson = restTemplate.postForEntity(GOOGLE_LOGIN_BASE_URI, requestEntity, String.class);
        GoogleOAuthLoginResponse response = (GoogleOAuthLoginResponse) mapping(stringifyJson.getBody(), GoogleOAuthLoginResponse.class);

        if(response != null) {
            return response;
        }
        else {
            throw new Exception("Google OAuth Login failed!");
        }
    }

    private Object mapping(String body, Class classType) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper.readValue(body, classType);
    }

    public synchronized String getAuthUri() {
        if(authUri == null) {
            Map<String, Object> map = Map.of(
                    "client_id", config.getClientId(),
                    "redirect_uri", config.getRedirectUri(),
                    "response_type", "code",
                    "scope", config.getScope().replaceAll(",", "%20"),
                    "access_type", "offline"
            );

            this.authUri = GOOGLE_AUTH_BASE_URI + map
                    .entrySet()
                    .stream()
                    .map(entry -> entry.getKey() + "=" + entry.getValue())
                    .collect(Collectors.joining("&"));
        }
        return authUri;
    }
}
