package graduation.spendiary.domain.finance;

import graduation.spendiary.domain.DatabaseSequence.SequenceGeneratorService;
import graduation.spendiary.exception.NoSuchContentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinanceService {

    @Autowired
    private FinanceRepository repo;

    public List<Finance> getAllOfUser(String userId) {
        return repo.findByUser(userId);
    }

    public Finance getById(long id) {
        if (!repo.existsById(id))
            throw new NoSuchContentException();
        return repo.findById(id).get();
    }

    /**
     * finance를 저장합니다.
     * @param finance 저장할 finance 정보
     * @param userId 사용자 ID
     * @return 저장된 finance ID
     */
    public Long save(Finance finance, String userId) {
        finance.setId(SequenceGeneratorService.generateSequence(Finance.SEQUENCE_NAME));
        finance.setUser(userId);
        Finance savedFinance = repo.save(finance);
        return savedFinance.getId();
    }
}
