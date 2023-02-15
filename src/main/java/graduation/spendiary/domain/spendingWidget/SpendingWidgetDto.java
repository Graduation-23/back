package graduation.spendiary.domain.spendingWidget;

import lombok.*;

import java.util.List;

@Data
@Builder
public class SpendingWidgetDto {
    private Long id;
    private List<SpendingWidgetItem> items;
    private Long totalCost;
}
