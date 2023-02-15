package graduation.spendiary.domain.spendingWidget;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class SpendingWidgetDto {
    private Long id;
    private String userId;
    private LocalDate date;
    private List<SpendingWidgetItem> items;
    private Long totalCost;
}
