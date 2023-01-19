package graduation.spendiary.controller;

import graduation.spendiary.domain.diary.Diary;
import graduation.spendiary.domain.diary.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/test/diarys")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryRepository diaryRepository;

    @GetMapping
    public String diarys(Model model) {
        List<Diary> diarys = diaryRepository.findAll();
        model.addAttribute("diarys", diarys);
        return "전체 조회";
    }

    @GetMapping("/{diaryId}")
    public String  diary(@PathVariable Long diaryId, Model model) {
        Diary diary = diaryRepository.findById(diaryId);
        model.addAttribute("diary", diary);
        return "상세 조회";
    }

    @PostMapping("/add")
    public String addDiary(Diary diary) {
        diaryRepository.save(diary);
        return "생성";
    }
}
