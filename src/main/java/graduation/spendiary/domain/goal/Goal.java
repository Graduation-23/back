package graduation.spendiary.domain.goal;

import lombok.*;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Document(collection = "Goal")
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Goal {

    @Transient
    public static final String SEQUENCE_NAME = "goal_sequence";

    @Field("goal_id")
    private Long id;

    @Field("user_id")
    private String user;

    @Field("goal_name")
    private String name;

    @Field("goal_amount")
    private Long amount;

    @Field("goal_state")
    private String state;

    @Field("goal_date")
    private LocalDate date;
}
