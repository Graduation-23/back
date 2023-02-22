package graduation.spendiary.domain.goal;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface GoalRepository extends MongoRepository<Goal, Long> {
    List<Goal> findByUser(String userId);

    @Query(value = "{'user_id': ?0, 'goal_type': ?1}")
    List<Goal> findByUserAndType(String userId, String type);

    @Query(value = "{'user_id': ?0, 'goal_month': ?1}")
    List<Goal> findByUserAndMonth(String userId, int mon);

    @Query(value = "{'user_id': ?0, 'goal_start': ?1}")
    List<Goal> findByUserAndDate(String userId, LocalDate date);

}
