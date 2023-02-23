package graduation.spendiary.domain.goal;

import graduation.spendiary.domain.DatabaseSequence.SequenceGeneratorService;
import graduation.spendiary.domain.spendingWidget.SpendingWidgetDto;
import graduation.spendiary.domain.spendingWidget.SpendingWidgetRepository;
import graduation.spendiary.domain.spendingWidget.SpendingWidgetService;
import graduation.spendiary.exception.NoSuchContentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GoalMonthService {
    @Autowired
    private GoalMonthRepository repo;
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
        return repo.findByUser(userId).stream()
                .map(this::getDto)
                .collect(Collectors.toList());
    }

    public GoalDto getById(long id) {
        Optional<GoalMonth> goalMonth = repo.findById(id);
        if (!repo.existsById(id))
            throw new NoSuchContentException();
        return this.getDto(goalMonth.get());
    }

    /**
     * 월간 목표
     */
    public boolean monthGoal(String userId, GoalMonth goalMonth) {
        LocalDate start = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());;
        LocalDate end = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());;

        if(repo.findByUserAndDate(userId, start).isEmpty()){

            goalMonth.setId(SequenceGeneratorService.generateSequence(GoalMonth.SEQUENCE_NAME));
            goalMonth.setStart(start);
            goalMonth.setEnd(end);
            goalMonth.setUser(userId);
            goalMonth.setState(state);
            goalMonth.setMonth(start.getMonthValue());
            repo.save(goalMonth);
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
}