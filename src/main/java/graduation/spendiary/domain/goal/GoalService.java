package graduation.spendiary.domain.goal;

import graduation.spendiary.domain.DatabaseSequence.SequenceGeneratorService;
import graduation.spendiary.exception.NoSuchContentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Locale;

@Service
public class GoalService {
    @Autowired
    private GoalRepository repo;
    private String month = "month";
    private String week = "week";

    public List<Goal> getAll(@AuthenticationPrincipal String userId) {
        return repo.findByUser(userId);
    }

    public Goal getById(long id) {
        if (!repo.existsById(id))
            throw new NoSuchContentException();
        return repo.findById(id).get();
    }

    /**
     * 월간 목표
     */
    public Goal monthGoal(String userId, Goal goal) {
        LocalDate start = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());;
        LocalDate end = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());;

        //String r1 = start.format(DateTimeFormatter.ISO_DATE);
        //String r2 = end.format(DateTimeFormatter.ISO_DATE);

        goal.setId(SequenceGeneratorService.generateSequence(Goal.SEQUENCE_NAME));
        goal.setType(month);
        goal.setStart(start);
        goal.setEnd(end);
        goal.setUser(userId);
        return repo.save(goal);
    }

    /**
     * 주간 목표
     */
    public Goal weekGoal(String userId, Goal goal) {
        int whatWeek = LocalDate.now().get(WeekFields.ISO.weekOfMonth());
        LocalDate start = LocalDate.now().with(TemporalAdjusters.dayOfWeekInMonth(whatWeek-1, DayOfWeek.MONDAY)); //특정 주차의 월요일 날짜
        LocalDate end = LocalDate.now().with(TemporalAdjusters.dayOfWeekInMonth(whatWeek, DayOfWeek.SUNDAY)); //일요일 날짜

        goal.setId(SequenceGeneratorService.generateSequence(Goal.SEQUENCE_NAME));
        goal.setType(week);
        goal.setStart(start);
        goal.setEnd(end);
        goal.setUser(userId);
        return repo.save(goal);
    }
}
