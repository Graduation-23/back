package graduation.spendiary.domain.achieve;

import graduation.spendiary.domain.DatabaseSequence.SequenceGeneratorService;
import graduation.spendiary.domain.goal.GoalMonth;
import graduation.spendiary.domain.goal.GoalMonthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AchieveService {

    @Autowired
    private AchieveRepository repo;
    @Autowired
    private GoalMonthRepository goalMonthRepo;

    public List<Achieve> getAllAchieve(String userId) {
        return repo.findByUser(userId);
    }

    public long getMonthAchieve(String userId) {
        Achieve achieve = repo.findById(userId);
        long monthCnt = goalMonthRepo.findByUser(userId).stream()
                .map(GoalMonth::getState)
                .filter(n -> n.contains("달성"))
                .count();
        achieve.setId(SequenceGeneratorService.generateSequence(Achieve.SEQUENCE_NAME));
        achieve.setMonthAchieve(monthCnt);
        repo.save(achieve);
        return monthCnt;
    }
}
