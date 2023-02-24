package graduation.spendiary.domain.goal;

import graduation.spendiary.domain.DatabaseSequence.SequenceGeneratorService;
import graduation.spendiary.domain.spendingWidget.SpendingWidgetService;
import graduation.spendiary.exception.NoSuchContentException;
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
public class GoalMonthService {
    @Autowired
    private GoalMonthRepository monthRepo;
    @Autowired
    private GoalWeekRepository weekRepo;
    @Autowired
    private SpendingWidgetService spendingWidgetService;
    @Autowired
    private GoalWeekService goalWeekService;

    private String state = "proceeding";

    public GoalDto getDto(GoalMonth goalMonth) {
        List<GoalWeek> goalWeekList = goalMonth.getWeekIds().stream()
                .map(goalWeekService::getById)
                .collect(Collectors.toList());
        return GoalDto.builder()
                .id(goalMonth.getId())
                .goalTitle(goalMonth.getName())
                .start(goalMonth.getStart())
                .end(goalMonth.getEnd())
                .goals(goalWeekList)
                .MonthAmount(goalMonth.getAmount())
                .build();
    }

    public List<GoalDto> getAll(String userId) {
        return monthRepo.findByUser(userId).stream()
                .map(this::getDto)
                .collect(Collectors.toList());
    }

    public GoalDto getById(long id) {
        Optional<GoalMonth> goalMonth = monthRepo.findById(id);
        if (!monthRepo.existsById(id))
            throw new NoSuchContentException();
        return this.getDto(goalMonth.get());
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
            monthRepo.save(goalMonth);
            return true;
        }else return false;


    }


    public boolean checkState(String userId, GoalMonth goalMonth) {
        /**
         * ToDo
         * total_cost < goal_amount => failed
         * total_cost >= goal_amount => achieved
         */
        return true;
    }


    public GoalWeek getGoalWeekById(Long id) {
        Optional<GoalWeek> goalWeeks = weekRepo.findById(id);
        if (goalWeeks.isEmpty()) throw new NoSuchContentException();
        return goalWeeks.get();
    }

    public boolean weekGoal(Long monthId, GoalWeek goalWeek) {
        // month 안에 있는 월간 목표 가져오기
        GoalMonth goalMonth = monthRepo.findById(monthId).get();
        long monthAmount = goalMonth.getAmount();
        long weekAmountSum = goalMonth.getWeekIds().stream()
                .map(this::getGoalWeekById)
                .map(GoalWeek::getAmount)
                .mapToLong(Long::longValue).sum();



        int whatWeek = LocalDate.now().get(WeekFields.ISO.weekOfMonth());
        LocalDate start = LocalDate.now().with(TemporalAdjusters.dayOfWeekInMonth(whatWeek - 1, DayOfWeek.MONDAY)); //특정 주차의 월요일 날짜
        LocalDate end = LocalDate.now().with(TemporalAdjusters.dayOfWeekInMonth(whatWeek, DayOfWeek.SUNDAY)); //일요일 날짜
        if (monthRepo.findByUserAndDate(monthId, start).isEmpty()) {
            goalWeek.setId(SequenceGeneratorService.generateSequence(GoalMonth.SEQUENCE_NAME));
            goalWeek.setStart(start);
            goalWeek.setEnd(end);
            goalWeek.setGoalMonth(monthId);
            goalWeek.setState(state);
            goalWeek.getAmount()
            monthRepo.save(goalWeek);
            return true;
        } else return false;
    }
}