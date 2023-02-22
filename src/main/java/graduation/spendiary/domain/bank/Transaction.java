package graduation.spendiary.domain.bank;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Transaction {
    private String bankName;
    private Long amount;
    private String transactionType;
    private String content;
}
