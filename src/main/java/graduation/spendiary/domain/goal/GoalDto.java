package graduation.spendiary.domain.goal;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class GoalDto {
    private Long id;

    private String goalTitle;

    @DateTimeFormat(pattern = "yyyy-MM-DD")
    private LocalDate start;

    @DateTimeFormat(pattern = "yyyy-MM-DD")
    private LocalDate end;

    private Long MonthAmount;

    private List<GoalWeek> goals;
}
