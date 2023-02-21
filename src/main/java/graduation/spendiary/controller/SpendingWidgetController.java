package graduation.spendiary.controller;

import graduation.spendiary.domain.diary.DiaryDto;
import graduation.spendiary.domain.spendingWidget.SpendingWidgetDto;
import graduation.spendiary.domain.spendingWidget.SpendingWidgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/widget")
@RequiredArgsConstructor
public class SpendingWidgetController {
    @Autowired
    private SpendingWidgetService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Long post(
            @AuthenticationPrincipal String userId,
            @RequestBody SpendingWidgetDto dto
    ) {
        return service.save(userId, dto);
    }

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<SpendingWidgetDto> getAll(@AuthenticationPrincipal String userId) {
        return service.getAllOfUser(userId);
    }

    @GetMapping(value = "/{widgetId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SpendingWidgetDto getById(@PathVariable Long widgetId) {
        return service.getDtoById(widgetId);
    }

    @GetMapping(value = "/last-week", consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<SpendingWidgetDto> getOfLastWeek(@AuthenticationPrincipal String userId) {
        return service.getOfLastWeek(userId);
    }

    @GetMapping(value = "/last-month", consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<SpendingWidgetDto> getOfLastMonth(@AuthenticationPrincipal String userId) {
        return service.getOfLastMonth(userId);
    }

    @GetMapping(value = "/date/{year}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<SpendingWidgetDto> getOfYear(
            @AuthenticationPrincipal String userId,
            @PathVariable int year
    ) {
        return service.getOfYear(userId, year);
    }

    @GetMapping(value = "/date/{year}/{month}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<SpendingWidgetDto> getOfMonth(
            @AuthenticationPrincipal String userId,
            @PathVariable int year,
            @PathVariable int month
    ) {
        return service.getOfMonth(userId, year, month);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Long put(
            @AuthenticationPrincipal String userId,
            @RequestBody SpendingWidgetDto dto
    ) {
        return service.edit(userId, dto);
    }

}
