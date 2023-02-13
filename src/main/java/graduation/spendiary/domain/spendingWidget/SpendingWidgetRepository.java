package graduation.spendiary.domain.spendingWidget;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SpendingWidgetRepository extends MongoRepository<SpendingWidget, Long> {
    List<SpendingWidget> findByDiaryId(long diaryId);
}
