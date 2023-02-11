package graduation.spendiary.controller;

import graduation.spendiary.domain.diary.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/diary")
@RequiredArgsConstructor
public class DiaryController {

    @Autowired
    private DiaryService diaryService;

    @GetMapping
    public List<DiaryDto> diarys(Model model) {
        List<DiaryDto> diaryDtos = diaryService.getDtoAll();
        model.addAttribute("diarys", diaryDtos);
        return diaryDtos;
    }

    @GetMapping("/{diaryId}")
    public DiaryDto getDiaryById(@PathVariable Long diaryId, Model model) {
        DiaryDto diaryDto = diaryService.getDtoById(diaryId);
        model.addAttribute("diary", diaryDto);
        return diaryDto;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DiaryDto addDiary(@AuthenticationPrincipal String userId, @ModelAttribute DiarySaveVo vo)
            throws IOException
    {
        return diaryService.save(vo, userId);
    }

    @GetMapping("/last-week")
    public List<DiaryDto> getOfLastWeek(@AuthenticationPrincipal String userId) {
        return diaryService.getOfLastWeek(userId);
    }

    @GetMapping("/last-month")
    public List<DiaryDto> getOfLastMonth(@AuthenticationPrincipal String userId) {
        return diaryService.getOfLastMonth(userId);
    }

    @GetMapping("/date/{year}")
    public List<DiaryDto> getOfYear(
            @AuthenticationPrincipal String userId,
            @PathVariable int year
    ) {
        return diaryService.getOfYear(userId, year);
    }

    @GetMapping("/date/{year}/{month}")
    public List<DiaryDto> getOfMonth(
            @AuthenticationPrincipal String userId,
            @PathVariable int year,
            @PathVariable int month
    ) {
        return diaryService.getOfMonth(userId, year, month);
    }

    @PutMapping(value = "/{diaryId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DiaryDto put(
            @AuthenticationPrincipal String userId,
            @PathVariable long diaryId,
            @ModelAttribute DiaryEditVo vo
    ) throws IOException
    {
        return diaryService.edit(diaryId, vo, userId);
    }
}
