package graduation.spendiary.domain.diary;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class DiarySaveVo {
    private String title;
    private String content;
    private List<MultipartFile> images;
    private String weather;
}
