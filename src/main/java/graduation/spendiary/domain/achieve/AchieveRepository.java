package graduation.spendiary.domain.achieve;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AchieveRepository extends MongoRepository<Achieve, Long> {
    List<Achieve> findByUser(String userId);

    Achieve findById(String userId);
}
