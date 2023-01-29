package graduation.spendiary.domain.diary;

import graduation.spendiary.util.responseFormat.ResponseFormat;
import graduation.spendiary.util.responseFormat.ServiceType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/diary")
public class DiaryController {

    @PostMapping("/add")
    public ResponseFormat<Long> save(DiarySaveVo vo) {
        try {

        }
        catch {
            return ResponseFormat.from(ServiceType.DIARY, "/diary/add", false, 0L);
        }
    }
}
