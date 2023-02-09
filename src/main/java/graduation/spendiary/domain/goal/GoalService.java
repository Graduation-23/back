package graduation.spendiary.domain.goal;

import graduation.spendiary.domain.DatabaseSequence.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoalService {
    @Autowired
    private GoalRepository repo;

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
}
