package graduation.spendiary.domain.diary;

import graduation.spendiary.domain.spendingWidget.SpendingWidgetDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class DiaryEditVo {
    private String title;
    private String content;
    private List<String> imageNames;
    private String weather;
    private Long thumbnailIdx;
    private List<MultipartFile> newImages;
    private SpendingWidgetDto widget;
}
