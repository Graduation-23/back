package graduation.spendiary.domain.achieve;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "Achieve")
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Achieve {

    @Transient
    public static final String SEQUENCE_NAME = "achieve_sequence";

    @MongoId
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;

    @Field("user_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String user;

    @Field("achieve_title")
    private String title;

    @Field("month_achieve")
    private long monthAchieve;

    @Field("week_achieve")
    private long weekAchieve;
}
