package graduation.spendiary.domain.spendingWidget;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpendingWidgetRepository extends MongoRepository<SpendingWidget, Long> {
    List<SpendingWidget> findByUser(String userId);
}
