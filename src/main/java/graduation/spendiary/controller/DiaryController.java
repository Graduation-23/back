package graduation.spendiary.controller;

import graduation.spendiary.domain.DatabaseSequence.SequenceGeneratorService;
import graduation.spendiary.domain.diary.Diary;
import graduation.spendiary.domain.diary.DiaryEditVo;
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
    public Diary getDiaryById(@PathVariable Long diaryId, Model model) {
        Diary diary = diaryService.getById(diaryId);
        model.addAttribute("diary", diary);
        return diary;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Diary addDiary(@AuthenticationPrincipal String userId, @ModelAttribute DiarySaveVo vo) {
        return diaryService.save(vo, userId).orElse(null);
    }

    @GetMapping("/last-week")
    public List<Diary> getOfLastWeek(@AuthenticationPrincipal String userId) {
        return diaryService.getOfLastWeek(userId);
    }

    @GetMapping("/last-month")
    public List<Diary> getOfLastMonth(@AuthenticationPrincipal String userId) {
        return diaryService.getOfLastMonth(userId);
    }

    @GetMapping("/date/{year}")
    public List<Diary> getOfYear(
            @AuthenticationPrincipal String userId,
            @PathVariable int year
    ) {
        return diaryService.getOfYear(userId, year);
    }

    @GetMapping("/date/{year}/{month}")
    public List<Diary> getOfMonth(
            @AuthenticationPrincipal String userId,
            @PathVariable int year,
            @PathVariable int month
    ) {
        return diaryService.getOfMonth(userId, year, month);
    }

    @PutMapping(value = "/{diaryId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Diary put(
            @AuthenticationPrincipal String userId,
            @PathVariable long diaryId,
            @ModelAttribute DiaryEditVo vo
    ) {
            return diaryService.edit(diaryId, vo, userId).orElse(null);
    }
}
