package graduation.spendiary.domain.bank;

import graduation.spendiary.security.openbank.BankRequestToken;
import graduation.spendiary.security.openbank.BankResponseToken;
import graduation.spendiary.security.openbank.OpenBankApiClient;
import graduation.spendiary.security.openbank.OpenbankConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OpenBankService {
    @Autowired
    private final OpenBankApiClient openBankApiClient;
    private final OpenbankConfig config;
    private String uri;
    private String OPEN_BANK_BASIC_URI = "https://testapi.openbanking.or.kr/oauth/2.0/authorize?";

    public BankResponseToken requestToken(BankRequestToken bankRequestToken) {
        return openBankApiClient.requestToken(bankRequestToken);
    }

    public synchronized String getUri(String userId) {
        if(uri == null) {
            Map<String, Object> map = Map.of(
                    "response_type", "code",
                    "client_id", config.getClientId(),
                    "redirect_uri", config.getRedirectUri(),
                    "scope", "login inquiry transfer".replaceAll(" ", "%20"),
                    "client_info", userId,
                    "state","b80BLsfigm9OokPTjy03elbJqRHOfGSY"
            );
            Map<String, Object> map2 = Map.of(
                    "auth_type", "0",
                    "cellphone_cert_yn", "Y",
                    "authorized_cert_yn", "Y",
                    "account_hold_auth_yn", "N",
                    "register_info", "A"
            );



            this.uri = OPEN_BANK_BASIC_URI + map
                    .entrySet()
                    .stream()
                    .map(entry -> entry.getKey() + "=" + entry.getValue())
                    .collect(Collectors.joining("&"))+
                    "&"+ map2
                    .entrySet()
                    .stream()
                    .map(entry -> entry.getKey() + "=" + entry.getValue())
                    .collect(Collectors.joining("&"));
        }
        return uri;
    }
}