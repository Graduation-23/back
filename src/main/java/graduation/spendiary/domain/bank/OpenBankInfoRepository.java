package graduation.spendiary.domain.bank;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpenBankInfoRepository extends MongoRepository<OpenBankInfo, String> {
}
