package graduation.spendiary.domain.finance;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinanceRepository extends MongoRepository<Finance, Long> {
    List<Finance> findByUser(String userId);
}
