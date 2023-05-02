package graduation.spendiary.controller;

import graduation.spendiary.domain.diary.DiaryDto;
import graduation.spendiary.domain.diary.DiaryEditVo;
import graduation.spendiary.domain.diary.DiaryService;
import graduation.spendiary.exception.ContentAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/diary")
@RequiredArgsConstructor
public class DiaryController {

    @Autowired
    private DiaryService diaryService;

    @GetMapping
    public List<DiaryDto> getAll(@AuthenticationPrincipal String userId) {
        return diaryService.getAllOfUser(userId);
    }

    @GetMapping(value = "/{diaryId}")
    public DiaryDto getDiaryById(@PathVariable Long diaryId, Model model) {
        DiaryDto diaryDto = diaryService.getDtoById(diaryId);
        model.addAttribute("diary", diaryDto);
        return diaryDto;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Long saveEmptyDiary(
            @AuthenticationPrincipal String userId,
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate diaryDate
    ) throws ContentAlreadyExistsException
    {
        return diaryService.saveEmptyDiary(userId, diaryDate);
    }

    @GetMapping(value = "/last-week")
    public List<DiaryDto> getOfLastWeek(@AuthenticationPrincipal String userId) {
        return diaryService.getOfLastWeek(userId);
    }

    @GetMapping(value = "/last-month")
    public List<DiaryDto> getOfLastMonth(@AuthenticationPrincipal String userId) {
        return diaryService.getOfLastMonth(userId);
    }

    @GetMapping(value = "/date/{year}")
    public List<DiaryDto> getOfYear(
            @AuthenticationPrincipal String userId,
            @PathVariable int year
    ) {
        return diaryService.getOfYear(userId, year);
    }

    @GetMapping(value = "/date/{year}/{month}")
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
    ) throws IOException {
        return diaryService.edit(diaryId, vo, userId);
    }

    @DeleteMapping
    public Message deleteDiary(@AuthenticationPrincipal String userId, @RequestParam("diaryId") Long diaryid) {
        diaryService.deleteDiary(userId, diaryid);
        return new Message("삭제 완료", true);
    }
}
