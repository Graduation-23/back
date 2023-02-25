package graduation.spendiary.domain.bank;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Document(collection = "Openbank")
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OpenBankInfo {
    @MongoId
    private String id;

    @Field("access_token")
    private String accessToken;

    @Field("refresh_token")
    private String refreshToken;

    @Field("fintech_nums")
    private List<String> fintechNums;

    @Field("user_seq_no")
    private String userSeqNo;
}
