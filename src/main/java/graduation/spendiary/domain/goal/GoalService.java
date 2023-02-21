package graduation.spendiary.domain.goal;

import graduation.spendiary.domain.DatabaseSequence.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class GoalService {
    @Autowired
    private GoalRepository repo;
    private String month;
    private String week;

    public List<Goal> getAll() {
        return repo.findAll();
    }

    public Goal getById(long id) {
        return repo.findById(id).get();
    }

    public Goal save(Goal goal, String userId) {
        goal.setId(SequenceGeneratorService.generateSequence(Goal.SEQUENCE_NAME));
        goal.setUser(userId);
        return repo.save(goal);
    }

    /**
     * 월간 목표
     */
    public Goal monthGoal(String userId, Goal goal) {
        LocalDate start = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());;
        LocalDate end = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());;
        goal.setId(SequenceGeneratorService.generateSequence(Goal.SEQUENCE_NAME));
        goal.setType(month);
        goal.setStart(start);
        goal.setEnd(end);
        goal.setUser(userId);
        return repo.save(goal);
    }
}
