package graduation.spendiary.domain.goal;

import graduation.spendiary.domain.DatabaseSequence.SequenceGeneratorService;
import graduation.spendiary.exception.NoSuchContentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Optional;

@Service
public class GoalWeekService {

    @Autowired
    private GoalWeekRepository repo;

    private String state = "proceeding";

    public List<GoalWeek> getThisGoal(Long goalMonthId) {
        return repo.findByGoalMonth(goalMonthId);
    }

    public GoalWeek getById(Long id) {
        Optional<GoalWeek> goalWeeks = repo.findById(id);
        if (goalWeeks.isEmpty()) throw new NoSuchContentException();
        return goalWeeks.get();
    }

    public Long save(GoalWeek goalWeek) {
        goalWeek.setId(SequenceGeneratorService.generateSequence(GoalWeek.SEQUENCE_NAME));
        return repo.save(goalWeek).getId();
    }

    public boolean weekGoal(Long monthId, GoalWeek goalWeek) {
        int whatWeek = LocalDate.now().get(WeekFields.ISO.weekOfMonth());
        LocalDate start = LocalDate.now().with(TemporalAdjusters.dayOfWeekInMonth(whatWeek - 1, DayOfWeek.MONDAY)); //특정 주차의 월요일 날짜
        LocalDate end = LocalDate.now().with(TemporalAdjusters.dayOfWeekInMonth(whatWeek, DayOfWeek.SUNDAY)); //일요일 날짜

        if (repo.findByUserAndDate(monthId, start).isEmpty()) {
            goalWeek.setId(SequenceGeneratorService.generateSequence(GoalMonth.SEQUENCE_NAME));
            goalWeek.setStart(start);
            goalWeek.setEnd(end);
            goalWeek.setGoalMonth(monthId);
            goalWeek.setState(state);
            repo.save(goalWeek);
            return true;
        } else return false;
    }
}
