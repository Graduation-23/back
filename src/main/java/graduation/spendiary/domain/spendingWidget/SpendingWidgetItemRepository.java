package graduation.spendiary.domain.spendingWidget;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpendingWidgetItemRepository extends MongoRepository<SpendingWidgetItem, Long> {
}
