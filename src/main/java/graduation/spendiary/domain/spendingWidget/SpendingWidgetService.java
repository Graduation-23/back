package graduation.spendiary.domain.spendingWidget;

import graduation.spendiary.domain.diary.DiaryDto;
import graduation.spendiary.domain.diary.DiaryService;
import graduation.spendiary.exception.NoSuchContentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 소비 내역(SpendingWidget)에 관한 서비스 클래스입니다.
 * @author 구본웅
 */
@Service
public class SpendingWidgetService {
    @Autowired
    private SpendingWidgetRepository repo;
    @Autowired
    private SpendingWidgetItemService itemService;

    /**
     * SpendingWidget을 DTO로 변환합니다.
     * itemId로부터 item도 함께 가져옵니다.
     * @param widget DTO로 변환할 SpendingWidget
     * @return
     */
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

    /**
     * 유저의 모든 SpendingWidget의 내용을 가져옵니다.
     * @return 발견된 SpendingWidget 리스트
     * todo: diary -> getId가 아닌 diaryId를 바로 가져올 수 있도록
     */
    public List<SpendingWidgetDto> getAllOfUser(String userId) {
        return repo.findByUser(userId).stream()
                .map(this::getDto)
                .collect(Collectors.toList());
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
     * @param dto 저장할 SpendingWidget의 DTO
     * @return 저장된 SpendingWidget의 ID
     */
    public Long save(String userId, SpendingWidgetDto dto) {
        // totalCost 계산
        long totalCost = dto.getItems().stream()
                .map(SpendingWidgetItem::getAmount)
                .mapToLong(Long::longValue).sum();

        // item 저장 및 id 가져오기
        List<Long> itemIds = dto.getItems().stream()
                .map(itemService::save)
                .collect(Collectors.toList());

        SpendingWidget widget = SpendingWidget.builder()
                .id(dto.getId())
                .user(userId)
                .itemIds(itemIds)
                .date(dto.getDate())
                .totalCost(totalCost)
                .build();

        SpendingWidget savedWidget = repo.save(widget);
        return savedWidget.getId();
    }

    /**
     * DTO의 정보를 통해 새 SpendingWidget을 수정합니다.
     * @param dto 수정할 SpendingWidget의 DTO
     * @return 저장된 SpendingWidget의 ID
     */
    public Long edit(String userId, SpendingWidgetDto dto)
        throws NoSuchContentException, NullPointerException
    {
        if (!repo.existsById(dto.getId()))
            throw new NoSuchContentException();
        return this.save(userId, dto);
    }

    public List<SpendingWidgetDto> getDtoByDateRange(String userId, LocalDate start, LocalDate end) {
        return repo.findByUserAndDateBetween(userId, start, end).stream()
                .map(this::getDto)
                .collect(Collectors.toList());
    }

    public List<SpendingWidgetDto> getOfLastWeek(String userId) {
        LocalDate now = LocalDate.now();
        LocalDate lastWeek = now.minusWeeks(1);
        return getDtoByDateRange(userId, lastWeek, now);
    }

    public List<SpendingWidgetDto> getOfLastMonth(String userId) {
        LocalDate now = LocalDate.now();
        LocalDate lastMonth = now.minusMonths(1);
        return getDtoByDateRange(userId, lastMonth, now);
    }

    public List<SpendingWidgetDto> getOfYear(String userId, int year) {
        LocalDate firstDay, lastDay;
        try {
            firstDay = LocalDate.of(year, 1, 1);
            lastDay = LocalDate.of(year, 12, 31);
        }
        catch (DateTimeException e) {
            return Collections.emptyList();
        }
        return getDtoByDateRange(userId, firstDay, lastDay);
    }

    public List<SpendingWidgetDto> getOfMonth(String userId, int year, int month) {
        LocalDate firstDay, lastDay;
        try {
            firstDay = LocalDate.of(year, month, 1);
            lastDay = LocalDate.of(year, month + 1, 1).minusDays(1);
        }
        catch (DateTimeException e) {
            return Collections.emptyList();
        }
        return getDtoByDateRange(userId, firstDay, lastDay);
    }
}
