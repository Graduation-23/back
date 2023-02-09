package graduation.spendiary.domain.spendingWidget;

import graduation.spendiary.domain.DatabaseSequence.SequenceGeneratorService;
import graduation.spendiary.exception.NoSuchContentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SpendingWidgetService {
    @Autowired
    private SpendingWidgetRepository repo;
    @Autowired
    private SpendingWidgetItemService itemService;

    public SpendingWidgetDto getDto(SpendingWidget widget) {
        List<SpendingWidgetItem> items = widget.getItemIds().stream()
                .map(itemService::getById)
                .collect(Collectors.toList());
        return SpendingWidgetDto.builder()
                .id(widget.getId())
                .items(items)
                .totalCost(widget.getTotalCost())
                .build();
    }

    public SpendingWidgetDto getDtoById(long id) {
        Optional<SpendingWidget> widgetOrNot = repo.findById(id);
        if (widgetOrNot.isEmpty())
            throw new NoSuchContentException();
        return this.getDto(widgetOrNot.get());
    }

    public SpendingWidget save(SpendingWidgetDto dto) {
        List<Long> itemIds = dto.getItems().stream()
                .map(itemService::save)
                .map(SpendingWidgetItem::getId)
                .collect(Collectors.toList());

        SpendingWidget widget = SpendingWidget.builder()
                .diaryId(dto.getDiaryId())
                .itemIds(itemIds)
                .build();
        widget.setId(SequenceGeneratorService.generateSequence(SpendingWidget.SEQUENCE_NAME));

        return repo.save(widget);
    }
}
