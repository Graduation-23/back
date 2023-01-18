package graduation.spendiary.controller;

import graduation.spendiary.domain.diary.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test/diarys")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryRepository diaryRepository;

    @GetMapping
    public String diarys() {
        return "GET diarys";
    }

    @PostMapping
    public String  createDiary() {
        return "POST diary";
    }

    @GetMapping("/{diaryId}")
    public String findDiary(@PathVariable String diaryId) {
        return "GET diaryId = " + diaryId;
    }
}
