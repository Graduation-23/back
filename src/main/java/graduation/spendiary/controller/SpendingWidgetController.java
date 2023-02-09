package graduation.spendiary.controller;

import graduation.spendiary.domain.spendingWidget.SpendingWidgetDto;
import graduation.spendiary.domain.spendingWidget.SpendingWidgetItem;
import graduation.spendiary.domain.spendingWidget.SpendingWidgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/widget")
@RequiredArgsConstructor
public class SpendingWidgetController {
    @Autowired
    private SpendingWidgetService service;

    @GetMapping
    public List<SpendingWidgetDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{widgetId}")
    public SpendingWidgetDto getById(@PathVariable Long widgetId) {
        return service.getDtoById(widgetId);
    }

}
