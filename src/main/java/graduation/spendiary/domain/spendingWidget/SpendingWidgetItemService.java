package graduation.spendiary.domain.spendingWidget;

import graduation.spendiary.domain.DatabaseSequence.SequenceGeneratorService;
import graduation.spendiary.exception.NoSuchContentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 소비 내역 항목(SpendingWidgetItem)에 관한 서비스 클래스
 * @author 구본웅
 */
@Service
public class SpendingWidgetItemService {
    @Autowired
    private SpendingWidgetItemRepository repo;

    /**
     * ID를 통해 SpendingWidgetItem을 가져옵니다.
     * @param id
     * @return 발견된 SpendingWidgetItem
     */
    public SpendingWidgetItem getById(Long id) {
        Optional<SpendingWidgetItem> widgetOrNot = repo.findById(id);
        if (widgetOrNot.isEmpty())
            throw new NoSuchContentException();
        return widgetOrNot.get();
    }

    /**
     * 새 SpendingWidgetItem을 저장합니다.
     * @param item 저장할 SpendingWidgetItem 정보
     * @return 새로 저장된 SpendingWidgetItem의 Id
     */
    public Long save(SpendingWidgetItem item) {
        item.setId(SequenceGeneratorService.generateSequence(SpendingWidgetItem.SEQUENCE_NAME));
        return repo.save(item).getId();
    }
}
