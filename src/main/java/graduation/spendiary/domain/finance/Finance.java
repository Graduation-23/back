package graduation.spendiary.domain.finance;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "Finance")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Finance {

    @Transient
    public static final String SEQUENCE_NAME = "finance_sequence";

    @Field("finance_id")
    private Long id;

    @Field("user_id")
    private String user;

    @Field("finance_type")
    private String type;

    @Field("finance_description")
    private String description;

    @Field("finance_anothername")
    private String anothername;

    @Field("finance_colorcode")
    private String colorcode;
}
