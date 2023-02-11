package graduation.spendiary.domain.spendingWidget;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
public class SpendingWidgetDto {
    private Long id;
    private Long diaryId;
    private List<SpendingWidgetItem> items;
    private long totalCost;
}
