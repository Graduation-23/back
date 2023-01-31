package graduation.spendiary.domain.finance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinanceService {

    @Autowired
    private FinanceRepository repo;

    public List<Finance> getAll() {
        return repo.findAll();
    }

    public Finance getById(long id) {
        return repo.findById(id).get();
    }

    public void save(Finance finance) {
        repo.save(finance);
    }
}