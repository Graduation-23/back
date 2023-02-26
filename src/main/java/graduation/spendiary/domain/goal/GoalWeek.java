package graduation.spendiary.domain.goal;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

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

    @Field("goal_week")
    private int week;

    @Field("goal_week_start")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @DateTimeFormat(pattern = "yyyy-MM-DD")
    private LocalDate start;

    @Field("goal_week_end")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @DateTimeFormat(pattern = "yyyy-MM-DD")
    private LocalDate end;

}
