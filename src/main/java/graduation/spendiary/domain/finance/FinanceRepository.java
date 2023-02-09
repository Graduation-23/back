package graduation.spendiary.domain.finance;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinanceRepository extends MongoRepository<Finance, Long> {
}
