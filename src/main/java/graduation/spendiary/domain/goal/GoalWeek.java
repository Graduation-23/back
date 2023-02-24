package graduation.spendiary.domain.goal;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Document(collection = "Goal_Week")
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoalWeek {
    @Transient
    public static final String SEQUENCE_NAME = "week_sequence";

    @Id
    @Field("goal_week_id")
    private Long id;

    @Field("goal_month_id")
    private Long goalMonth;

    @Field("goal_week_name")
    private String name;

    @Field("goal_week_amount")
    private Long amount;

    @Field("goal_week_state")
    private String state;

    @Field("goal_week_start")
    private LocalDate start;

    @Field("goal_week_end")
    private LocalDate end;

}
