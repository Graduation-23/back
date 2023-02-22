package graduation.spendiary.domain.goal;

import graduation.spendiary.domain.DatabaseSequence.SequenceGeneratorService;
import graduation.spendiary.exception.NoSuchContentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

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
    public boolean monthGoal(String userId, Goal goal) {
        LocalDate start = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());;
        LocalDate end = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());;

        if(repo.findByUserAndMonth(userId, start.getMonthValue()).isEmpty() && repo.findByUserAndType(userId, month).isEmpty()){
            //같은 달 중 type이 month인 것이 존재하는가
            goal.setId(SequenceGeneratorService.generateSequence(Goal.SEQUENCE_NAME));
            goal.setType(month);
            goal.setMon(start.getMonthValue());
            goal.setStart(start);
            goal.setEnd(end);
            goal.setUser(userId);
            repo.save(goal);
            return true;
        }else return false;


    }

    /**
     * 주간 목표
     */
    public boolean weekGoal(String userId, Goal goal) {
        int whatWeek = LocalDate.now().get(WeekFields.ISO.weekOfMonth());
        LocalDate start = LocalDate.now().with(TemporalAdjusters.dayOfWeekInMonth(whatWeek-1, DayOfWeek.MONDAY)); //특정 주차의 월요일 날짜
        LocalDate end = LocalDate.now().with(TemporalAdjusters.dayOfWeekInMonth(whatWeek, DayOfWeek.SUNDAY)); //일요일 날짜

        if(repo.findByUserAndType(userId, week).isEmpty() && repo.findByUserAndDate(userId, start).isEmpty()){
            goal.setId(SequenceGeneratorService.generateSequence(Goal.SEQUENCE_NAME));
            goal.setType(week);
            goal.setMon(start.getMonthValue());
            goal.setStart(start);
            goal.setEnd(end);
            goal.setUser(userId);
            repo.save(goal);
            return true;
        } else return false;

        /**
         * ToDo
         * 당월의 월간 목표에 amount >= 같은 달의 Type이 week인 amount의 합
         */

    }
}
