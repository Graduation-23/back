package graduation.spendiary.domain.spendingWidget;

import graduation.spendiary.domain.DatabaseSequence.SequenceGeneratorService;
import graduation.spendiary.exception.NoSuchContentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 소비 내역(SpendingWidget)에 관한 서비스 클래스입니다.
 */
@Service
public class SpendingWidgetService {
    @Autowired
    private SpendingWidgetRepository repo;
    @Autowired
    private SpendingWidgetItemService itemService;

    /**
     * 모든 SpendingWidget의 내용을 가져옵니다.
     * @return 발견된 SpendingWidget 리스트
     */
    public List<SpendingWidgetDto> getAll() {
        return repo.findAll().stream()
                .map(this::getDto)
                .collect(Collectors.toList());
    }

    /**
     * SpendingWidget을 DTO로 변환합니다.
     * itemId로부터 item도 함께 가져옵니다.
     * @param widget
     * @return
     */
    public SpendingWidgetDto getDto(SpendingWidget widget) {
        List<SpendingWidgetItem> items = widget.getItemIds().stream()
                .map(itemService::getById)
                .collect(Collectors.toList());
        return SpendingWidgetDto.builder()
                .id(widget.getId())
                .diaryId(widget.getDiaryId())
                .items(items)
                .totalCost(widget.getTotalCost())
                .build();
    }

    /**
     * SpendingWidget의 ID를 이용해 DTO를 가져옵니다.
     * @param id
     * @return 발견된 SpendingWidget
     * @throws NoSuchContentException id에 해당하는 SpendingWidget이 없음
     */
    public SpendingWidgetDto getDtoById(long id)
        throws NoSuchContentException
    {
        Optional<SpendingWidget> widgetOrNot = repo.findById(id);
        if (widgetOrNot.isEmpty())
            throw new NoSuchContentException();
        return this.getDto(widgetOrNot.get());
    }

    /**
     * DTO의 정보를 통해 새 SpendingWidget을 저장합니다.
     * @param dto
     * @return 저장된 SpendingWidget (entity)
     */
    public SpendingWidget save(SpendingWidgetDto dto) {
        List<SpendingWidgetItem> items = dto.getItems().stream()
                .map(itemService::save)
                .collect(Collectors.toList());
        // totalCost 계산
        long totalCost = items.stream()
                .map(SpendingWidgetItem::getAmount)
                .mapToLong(Long::longValue).sum();
        // id 가져오기
        List<Long> itemIds = items.stream()
                .map(SpendingWidgetItem::getId)
                .collect(Collectors.toList());

        SpendingWidget widget = SpendingWidget.builder()
                .diaryId(dto.getDiaryId())
                .itemIds(itemIds)
                .totalCost(totalCost)
                .build();
        widget.setId(SequenceGeneratorService.generateSequence(SpendingWidget.SEQUENCE_NAME));

        return repo.save(widget);
    }
}
