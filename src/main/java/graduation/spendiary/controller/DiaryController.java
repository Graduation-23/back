package graduation.spendiary.controller;

import graduation.spendiary.domain.DatabaseSequence.SequenceGeneratorService;
import graduation.spendiary.domain.diary.Diary;
import graduation.spendiary.domain.diary.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diarys")
@RequiredArgsConstructor
public class DiaryController {

    @Autowired
    private DiaryService diaryService;

    @GetMapping
    public List<Diary> diarys(Model model) {
        List<Diary> diarys = diaryService.getAll();
        model.addAttribute("diarys", diarys);
        return diarys;
    }

    @GetMapping("/{diaryId}")
    public Diary  diary(@PathVariable Long diaryId, Model model) {
        Diary diary = diaryService.getById(diaryId);
        model.addAttribute("diary", diary);
        return diary;
    }

    @PostMapping("/add")
    public String addDiary(@RequestBody Diary diary) {
        diary.setId(SequenceGeneratorService.generateSequence(Diary.SEQUENCE_NAME));
        diaryService.save(diary);
        return "생성";
    }
}
