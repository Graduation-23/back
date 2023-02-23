package graduation.spendiary.domain.goal;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface GoalMonthRepository extends MongoRepository<GoalMonth, Long> {
    List<GoalMonth> findByUser(String userId);

    @Query(value = "{'user_id': ?0, 'goal_month_start': ?1}")
    List<GoalMonth> findByUserAndDate(String userId, LocalDate date);

}
