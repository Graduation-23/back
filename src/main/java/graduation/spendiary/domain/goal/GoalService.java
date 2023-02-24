package graduation.spendiary.domain.goal;

import graduation.spendiary.domain.DatabaseSequence.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Optional;

@Service
public class GoalService {
    @Autowired
    private GoalMonthRepository monthRepo;
    @Autowired
    private GoalWeekRepository weekRepo;
    private String state = "proceeding";

    public List<GoalMonth> getAll(String userId) {
        return monthRepo.findByUser(userId);
    }

    public List<GoalWeek> getThisGoal(Long goalMonthId) {
        return weekRepo.findByGoalMonth(goalMonthId);
    }

    public GoalWeek getWeekById(Long id) {
        Optional<GoalWeek> goalWeeks = weekRepo.findById(id);
        return goalWeeks.get();
    }

    public GoalMonth getMonthById(Long id) {
        Optional<GoalMonth> goalMonth = monthRepo.findById(id);
        return goalMonth.get();
    }

    /**
     * 월간 목표
     */
    public boolean monthGoal(String userId, GoalMonth goalMonth) {
        LocalDate start = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());;
        LocalDate end = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());;

        if(monthRepo.findByUserAndDate(userId, start).isEmpty()){

            goalMonth.setId(SequenceGeneratorService.generateSequence(GoalMonth.SEQUENCE_NAME));
            goalMonth.setStart(start);
            goalMonth.setEnd(end);
            goalMonth.setUser(userId);
            goalMonth.setState(state);
            goalMonth.setMonth(start.getMonthValue());
            goalMonth.setYear(start.getYear());
            monthRepo.save(goalMonth);
            return true;
        }else return false;


    }


    public boolean weekGoal(Long monthId, GoalWeek goalWeek) {
        int whatWeek = LocalDate.now().get(WeekFields.ISO.weekOfMonth());
        LocalDate start = LocalDate.now().with(TemporalAdjusters.dayOfWeekInMonth(whatWeek - 1, DayOfWeek.MONDAY)); //특정 주차의 월요일 날짜
        LocalDate end = LocalDate.now().with(TemporalAdjusters.dayOfWeekInMonth(whatWeek, DayOfWeek.SUNDAY)); //일요일 날짜

        GoalMonth goalMonth = monthRepo.findById(monthId).get();
        long monthAmount = goalMonth.getAmount();
        long weekAmountSum = goalMonth.getWeekIds().stream()
                .map(this::getWeekById)
                .map(GoalWeek::getAmount)
                .mapToLong(Long::longValue).sum();
        weekAmountSum += goalWeek.getAmount();

        if (weekRepo.findByUserAndDate(monthId, start).isEmpty() && monthAmount >= weekAmountSum) {
            goalWeek.setId(SequenceGeneratorService.generateSequence(GoalMonth.SEQUENCE_NAME));
            goalWeek.setStart(start);
            goalWeek.setEnd(end);
            goalWeek.setGoalMonth(monthId);
            goalWeek.setState(state);
            weekRepo.save(goalWeek);
            return true;
        } else return false;
    }

    public void insertWeekId(Long monthId, GoalWeek goalWeek) {
        GoalMonth goalMonth = monthRepo.findById(monthId).get();
        goalMonth.getWeekIds().add(goalWeek.getId());
        monthRepo.save(goalMonth);
    }
}