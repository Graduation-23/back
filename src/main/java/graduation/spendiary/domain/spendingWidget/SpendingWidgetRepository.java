package graduation.spendiary.domain.spendingWidget;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SpendingWidgetRepository extends MongoRepository<SpendingWidget, Long> {
    List<SpendingWidget> findByUser(String userId);

    @Query(value = "{'user_id': ?0, 'date': {$gte: ?1, $lte: ?2, $tz:'Asia/Seoul'}}")
    List<SpendingWidget> findByUserAndDateBetween(String userId, LocalDate start, LocalDate end);
}
