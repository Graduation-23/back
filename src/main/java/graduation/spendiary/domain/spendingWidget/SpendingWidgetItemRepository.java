package graduation.spendiary.domain.spendingWidget;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpendingWidgetItemRepository extends MongoRepository<SpendingWidgetItem, Long> {
}
