package graduation.spendiary.domain.achieve;

import graduation.spendiary.domain.DatabaseSequence.SequenceGeneratorService;
import graduation.spendiary.domain.goal.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AchieveService {

    @Autowired
    private AchieveRepository repo;
    @Autowired
    private GoalMonthRepository goalMonthRepo;
    @Autowired
    private GoalWeekRepository goalWeekRepo;
    @Autowired
    private GoalService goalService;

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

    public long getWeekAchieve(String userId) {
        Achieve achieve = repo.findById(userId);
        List<List<Long>> weekIds = goalMonthRepo.findByUser(userId).stream()
                .map(GoalMonth::getWeekIds)
                .collect(Collectors.toList());

        long weekCnt = 0;
        for (List<Long> n : weekIds) {
            weekCnt += n.stream()
                    .map(goalService::getWeekById)
                    .map(GoalWeek::getState)
                    .filter(a -> ((String) a).contains("달성"))
                    .count();
        }
        achieve.setWeekAchieve(weekCnt);
        repo.save(achieve);
        return weekCnt;
    }
}
