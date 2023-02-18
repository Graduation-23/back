package graduation.spendiary.domain.spendingWidget;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class SpendingWidgetDto {
    private Long id;
    private @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date;
    private List<SpendingWidgetItem> items;
    private Long totalCost;
}
