package graduation.spendiary.domain.goal;

import graduation.spendiary.domain.DatabaseSequence.SequenceGeneratorService;
import graduation.spendiary.domain.spendingWidget.SpendingWidgetDto;
import graduation.spendiary.domain.spendingWidget.SpendingWidgetRepository;
import graduation.spendiary.domain.spendingWidget.SpendingWidgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GoalService {
    @Autowired
    private GoalMonthRepository monthRepo;
    @Autowired
    private GoalWeekRepository weekRepo;
    @Autowired
    private SpendingWidgetRepository spendRepo;
    @Autowired
    private SpendingWidgetService widgetService;
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
        LocalDate start = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        LocalDate end = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());

        if(monthRepo.findByUserAndDate(userId, start).isEmpty()){

            goalMonth.setId(SequenceGeneratorService.generateSequence(GoalMonth.SEQUENCE_NAME));
            goalMonth.setStart(start);
            goalMonth.setEnd(end);
            goalMonth.setUser(userId);
            goalMonth.setState(state);
            goalMonth.setMonth(start.getMonthValue());
            goalMonth.setYear(start.getYear());
            goalMonth.setWeekIds(null);
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

    public boolean checkMonthState(String userId) {
        LocalDate start = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        LocalDate end = LocalDate.now().minusMonths(1).withDayOfMonth(start.lengthOfMonth());

        List<Long> spendingWidget = spendRepo.findByUserAndDateBetween(userId, start, end).stream()
                .map(widgetService::getDto)
                .map(SpendingWidgetDto::getTotalCost)
                .collect(Collectors.toList());
        long total_cost = spendingWidget.stream().mapToLong(Long::longValue).sum();

        List<Long> goalMonthAmount = monthRepo.findByUserAndDate(userId, start).stream()
                .map(GoalMonth::getAmount)
                .collect(Collectors.toList());
        long monthAmount = goalMonthAmount.stream().mapToLong(Long::longValue).sum();

        GoalMonth goalMonth = monthRepo.findByUserAndGoalMonth(userId, start);
        String achieved = "달성";
        String failed = "실패";

        if(total_cost <= monthAmount) {
            goalMonth.setState(achieved);
            monthRepo.save(goalMonth);
        }
        else {
            goalMonth.setState(failed);
            monthRepo.save(goalMonth);
        }
        return true;
    }

    public boolean checkWeekState(String userId) {
        LocalDate start = LocalDate.now().minusWeeks(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate end = start.plusDays(6);

        List<Long> spendingWidget = spendRepo.findByUserAndDateBetween(userId, start, end).stream()
                .map(widgetService::getDto)
                .map(SpendingWidgetDto::getTotalCost)
                .collect(Collectors.toList());
        long total_cost = spendingWidget.stream().mapToLong(Long::longValue).sum();

        Long monthId = monthRepo.findByUserAndGoalMonth(userId, start.with(TemporalAdjusters.firstDayOfMonth())).getId();
        List<Long> goalWeekAmount = weekRepo.findByUserAndDate(monthId, start).stream()
                .map(GoalWeek::getAmount)
                .collect(Collectors.toList());
        long weekAmount = goalWeekAmount.stream().mapToLong(Long::longValue).sum();

        GoalWeek goalWeek = weekRepo.findByUserAndWeek(monthId, start);
        String achieved = "달성";
        String failed = "실패";

        if(total_cost <= weekAmount) {
            goalWeek.setState(achieved);
            weekRepo.save(goalWeek);
        } else {
            goalWeek.setState(failed);
            weekRepo.save(goalWeek);
        }
        return true;
    }
}