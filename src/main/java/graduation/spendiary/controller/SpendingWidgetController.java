package graduation.spendiary.controller;

import graduation.spendiary.domain.spendingWidget.SpendingWidgetDto;
import graduation.spendiary.domain.spendingWidget.SpendingWidgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/widget")
@RequiredArgsConstructor
public class SpendingWidgetController {
    @Autowired
    private SpendingWidgetService service;

    @PostMapping
    public Long post(
            @AuthenticationPrincipal String userId,
            @ModelAttribute SpendingWidgetDto dto
    ) {
        return service.save(userId, dto);
    }

    @GetMapping
    public List<SpendingWidgetDto> getAll(@AuthenticationPrincipal String userId) {
        return service.getAllOfUser(userId);
    }

    @GetMapping("/{widgetId}")
    public SpendingWidgetDto getById(@PathVariable Long widgetId) {
        return service.getDtoById(widgetId);
    }

    @PutMapping
    public Long put(
            @AuthenticationPrincipal String userId,
            @ModelAttribute SpendingWidgetDto dto
    ) {
        return service.edit(userId, dto);
    }

}
