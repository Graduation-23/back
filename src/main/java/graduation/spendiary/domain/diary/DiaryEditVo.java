package graduation.spendiary.domain.diary;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@RequiredArgsConstructor
public class DiaryEditVo {
    private String title;
    private String content;
    private List<String> imageNames;
    private String weather;
    private List<MultipartFile> newImages;
}
