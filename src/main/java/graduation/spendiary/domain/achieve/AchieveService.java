package graduation.spendiary.domain.achieve;

import graduation.spendiary.domain.DatabaseSequence.SequenceGeneratorService;
import graduation.spendiary.domain.goal.GoalMonth;
import graduation.spendiary.domain.goal.GoalMonthRepository;
import graduation.spendiary.domain.goal.GoalWeek;
import graduation.spendiary.domain.goal.GoalWeekRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Service
public class AchieveService {

    @Autowired
    private AchieveRepository repo;
    @Autowired
    private GoalMonthRepository goalMonthRepo;
    @Autowired
    private GoalWeekRepository goalWeekRepo;

    public List<Achieve> getAllAchieve(String userId) {
        return repo.findByUser(userId);
    }

    public boolean createAchieve(String userId, Achieve achieve) {
        if(repo.findByUser(userId).isEmpty()) {
            achieve.setId(SequenceGeneratorService.generateSequence(Achieve.SEQUENCE_NAME));
            achieve.setTitle(achieve.getTitle());
            achieve.setUser(userId);
            achieve.setMonthAchieve(0);
            achieve.setWeekAchieve(0);
            repo.save(achieve);
            return true;
        }
        return false;
    }

    public boolean getMonthAchieve(String userId) {
        Achieve achieve = repo.findById(userId);
        long monthCnt = goalMonthRepo.findByUser(userId).stream()
                .map(GoalMonth::getState)
                .filter(n -> n.contains("달성"))
                .count();
        achieve.setMonthAchieve(monthCnt);
        repo.save(achieve);
        return true;
    }
}
