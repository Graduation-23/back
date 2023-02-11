package graduation.spendiary.domain.diary;

import graduation.spendiary.domain.spendingWidget.SpendingWidgetDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DiaryDto {
    private Long id;
    private String title;
    private String content;
    private List<String> imageNames;
    private String weather;
    private SpendingWidgetDto widget;
}
