package graduation.spendiary.controller;

import graduation.spendiary.domain.DatabaseSequence.SequenceGeneratorService;
import graduation.spendiary.domain.diary.Diary;
import graduation.spendiary.domain.diary.DiarySaveVo;
import graduation.spendiary.domain.diary.DiaryService;
import graduation.spendiary.domain.user.User;
import graduation.spendiary.security.jwt.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Diary addDiary(@ModelAttribute DiarySaveVo vo, @AuthenticationPrincipal String userId) {
        return diaryService.save(vo, userId).orElse(null);
    }
}
