package graduation.spendiary.domain.goal;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "Goal_Month")
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GoalMonth {
    @Transient
    public static final String SEQUENCE_NAME = "month_sequence";

    @MongoId
    private Long id;

    @Field("user_id")
    private String user;

    @Field("goal_month_name")
    private String name;

    @Field("goal_month_amount")
    private Long amount;

    @Field("goal_month_state")
    private String state;

    @Field("goal_month")
    private int month;

    @Field("goal_month_start")
    @DateTimeFormat(pattern = "yyyy-MM-DD")
    private LocalDate start;

    @Field("goal_month_end")
    @DateTimeFormat(pattern = "yyyy-MM-DD")
    private LocalDate end;

    @Field("week_ids")
    private List<Long> weekIds;
}
