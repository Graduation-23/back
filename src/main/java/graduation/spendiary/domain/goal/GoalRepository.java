package graduation.spendiary.domain.goal;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalRepository extends MongoRepository<Goal, Long> {
    List<Goal> findByUser(String userId);
}
