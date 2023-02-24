package graduation.spendiary.domain.goal;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface GoalWeekRepository extends MongoRepository<GoalWeek, Long> {
    List<GoalWeek> findByGoalMonth(Long monthId);

    @Query(value = "{'goal_month_id': ?0, 'goal_week_start': ?1}")
    List<GoalWeek> findByUserAndDate(Long monthId, LocalDate date);
}
