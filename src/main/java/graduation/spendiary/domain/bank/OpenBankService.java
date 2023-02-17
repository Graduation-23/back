package graduation.spendiary.domain.bank;

import graduation.spendiary.security.openbank.BankRequestToken;
import graduation.spendiary.security.openbank.BankResponseToken;
import graduation.spendiary.security.openbank.OpenBankApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OpenBankService {
    private final OpenBankApiClient openBankApiClient;

    public BankResponseToken requestToken(BankRequestToken bankRequestToken) {
        return openBankApiClient.requestToken(bankRequestToken);
    }
}