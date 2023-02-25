package graduation.spendiary.domain.achieve;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AchieveRepository extends MongoRepository<Achieve, Long> {
    List<Achieve> findByUser(String userId);

    @Query(value = "{'user_id': ?0}")
    Achieve findById(String userId);
}
