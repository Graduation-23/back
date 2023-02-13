package graduation.spendiary.domain.diary;

import graduation.spendiary.domain.spendingWidget.SpendingWidget;
import graduation.spendiary.domain.spendingWidget.SpendingWidgetDto;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class DiarySaveVo {
    private String title;
    private String content;
    private List<MultipartFile> images;
    private String weather;
    private SpendingWidgetDto widget;
}
