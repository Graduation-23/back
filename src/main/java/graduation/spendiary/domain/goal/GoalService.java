package graduation.spendiary.domain.goal;

import graduation.spendiary.domain.DatabaseSequence.SequenceGeneratorService;
import graduation.spendiary.domain.spendingWidget.SpendingWidgetDto;
import graduation.spendiary.domain.spendingWidget.SpendingWidgetRepository;
import graduation.spendiary.domain.spendingWidget.SpendingWidgetService;
import graduation.spendiary.domain.user.UserService;
import graduation.spendiary.exception.ContentAlreadyExistsException;
import graduation.spendiary.exception.GoalAmountExceededException;
import graduation.spendiary.exception.NoSuchContentException;
import graduation.spendiary.util.DateUtil;
import graduation.spendiary.util.LocalDatePeriod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 월간/주간 소비량 목표를 관리하는 클래스입니다.
 * @author 구본웅, 정민영
 */
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
    @Autowired
    private UserService userService;

    private final String STATE_PROCEEDING = "진행중";
    private final String STATE_ACHEIVED = "달성";
    private final String STATE_FAILED = "실패";
    private final String DEFAULT_NAME_MONTH = "월간";
    private final String DEFAULT_NAME_WEEK = "주간";

    public List<GoalMonth> getAll(String userId) {
        return monthRepo.findByUser(userId);
    }

    public List<GoalWeek> getThisGoal(Long goalMonthId) {
        return weekRepo.findByGoalMonth(goalMonthId);
    }

    public GoalWeek getWeekById(Long id) {
        Optional<GoalWeek> goalWeekOptional = weekRepo.findById(id);
        if (goalWeekOptional.isEmpty())
            throw new NoSuchContentException();
        return goalWeekOptional.get();
    }

    public GoalMonth getMonthById(Long id) {
        Optional<GoalMonth> goalMonthOptional = monthRepo.findById(id);
        if (goalMonthOptional.isEmpty())
            throw new NoSuchContentException();
        return goalMonthOptional.get();
    }

    public GoalMonth getGoalMonthOf(String userId, int year, int month) {
        LocalDate monthStart = LocalDate.of(year, month, 1);
        List<GoalMonth> monthGoals = monthRepo.findByUserAndDate(userId, monthStart);
        if (monthGoals.size() == 0)
            return null;
        else
            return monthGoals.get(0);
    }
    
    /**
     * 주어진 월에 해당하는 월간 목표를 만듭니다.
     * @param userId
     * @param goalMonth
     * @return 생성된 월간 목표 ID, 실패라면 -1.
     */
    public Long monthGoal(String userId, int year, int month, GoalMonth goalMonth) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = LocalDate.of(year, month + 1, 1).minusDays(1);

        if (!monthRepo.findByUserAndDate(userId, start).isEmpty())
            throw new NoSuchContentException();

        goalMonth.setId(SequenceGeneratorService.generateSequence(GoalMonth.SEQUENCE_NAME));
        goalMonth.setStart(start);
        goalMonth.setEnd(end);
        goalMonth.setUser(userId);
        goalMonth.setState(STATE_PROCEEDING);
        goalMonth.setName(DEFAULT_NAME_MONTH);
        goalMonth.setMonth(month);
        goalMonth.setYear(year);
        GoalMonth savedGoal = monthRepo.save(goalMonth);
        return savedGoal.getId();
    }

    /**
     * 주어진 월간 목표의 달과 주에 해당하는 주간 목표를 만듭니다.
     * @param monthId 월간 목표 ID
     * @param goalWeek 주간 목표 정보
     * @return 생성된 주간 목표의 ID, 저장 실패 시 -1.
     */
    public Long weekGoal(Long monthId, GoalWeek goalWeek) {
        int week = goalWeek.getWeek();
        GoalMonth goalMonth = getMonthById(monthId);

        LocalDatePeriod period = DateUtil.getDatePeriod(goalMonth.getYear(), goalMonth.getMonth(), week);
        assert period != null;
        LocalDate start = period.getStart();
        LocalDate end = period.getEnd();

        if (!weekRepo.findByUserAndDate(monthId, start).isEmpty())
            throw new ContentAlreadyExistsException();

        if (goalMonth.getAmount() < this.getWeekGoalAmountSum(goalMonth) + goalWeek.getAmount())
            throw new GoalAmountExceededException();

        goalWeek.setId(SequenceGeneratorService.generateSequence(GoalWeek.SEQUENCE_NAME));
        goalWeek.setStart(start);
        goalWeek.setEnd(end);
        goalWeek.setGoalMonth(monthId);
        goalWeek.setState(STATE_PROCEEDING);
        goalWeek.setName(DEFAULT_NAME_WEEK);
        goalWeek.setWeek(week);
        GoalWeek savedGoal = weekRepo.save(goalWeek);
        return savedGoal.getId();
    }

    public void insertWeekId(Long monthId, GoalWeek goalWeek) {
        GoalMonth goalMonth = monthRepo.findById(monthId).get();
        goalMonth.getWeekIds().add(goalWeek.getId());
        monthRepo.save(goalMonth);
    }

    /**
     * 현재 시간을 기준으로 새 월간 목표를 만듭니다.
     * @param userId 사용자 ID
     * @param goal 월간 목표 정보
     * @return 성공 여부
     */
    public Long createMonthGoalOfNow(String userId, GoalMonth goal) {
        LocalDate now = LocalDate.now();
        if (goal.getWeekIds() == null)
            goal.setWeekIds(Collections.emptyList());
        Long monthGoalId = monthGoal(userId, now.getYear(), now.getMonthValue(), goal);

        for (int i=1; i<=4; i++) {
            GoalWeek emptyGoalWeek = GoalWeek.builder()
                    .week(i)
                    .amount(0L)
                    .build();
            Long weekGoalId = weekGoal(monthGoalId, emptyGoalWeek);
            insertWeekId(monthGoalId, getWeekById(weekGoalId));
        }
        return monthGoalId;
    }

    /**
     * 주간 목표를 수정합니다.
     * @param id 주간 목표 ID
     * @param amount 주간 목표량
     */
    public void editWeekGoal(Long id, Long amount) {
        GoalWeek targetGoal = this.getWeekById(id);
        GoalMonth parentGoal = this.getMonthById(targetGoal.getGoalMonth());
        Long parentWeekSum = this.getWeekGoalAmountSum(parentGoal);
        if (parentWeekSum - targetGoal.getAmount() + amount > parentGoal.getAmount())
            throw new GoalAmountExceededException();

        targetGoal.setAmount(amount);
        weekRepo.save(targetGoal);
    }

    /**
     * 사용자의 월간 목표 상태를 확인하고 DB를 업데이트합니다.
     * @param userId 사용자 ID
     * @param monthId 월간 목표 ID
     * @return 성공 여부
     */
    public boolean checkMonthState(String userId, Long monthId) {
        GoalMonth goalMonth = monthRepo.findById(monthId).get();

        LocalDate start = goalMonth.getStart();
        LocalDate end = goalMonth.getEnd();
        LocalDate now = LocalDate.now();
        LocalDate userCreated = userService.getUser(userId).getCreated();

        long totalCost = spendRepo.findByUserAndDateBetween(userId, start, end.plusDays(1)).stream()
                .map(widgetService::getDto)
                .map(SpendingWidgetDto::getTotalCost)
                .mapToLong(Long::longValue)
                .sum();

        long monthAmountSum = monthRepo.findByUserAndDate(userId, start).stream()
                .map(GoalMonth::getAmount)
                .mapToLong(Long::longValue).sum();

        if (userCreated.isAfter(end))
            goalMonth.setState(STATE_FAILED);
        else if (totalCost > monthAmountSum)
            goalMonth.setState(STATE_FAILED);
        else if (end.isBefore(now))
            goalMonth.setState(STATE_ACHEIVED);
        else
            goalMonth.setState(STATE_PROCEEDING);
        monthRepo.save(goalMonth);
        return true;
    }

    /**
     * 사용자의 월간 목표 상태를 확인하고 DB를 업데이트합니다.
     * @param userId 사용자 ID
     * @param weekId 주간 목표 ID
     * @return 성공 여부
     */
    public boolean checkWeekState(String userId, Long weekId) {
        GoalWeek goalWeek = getWeekById(weekId);

        LocalDate start = goalWeek.getStart();
        LocalDate end = goalWeek.getEnd();
        LocalDate now = LocalDate.now();
        LocalDate userCreated = userService.getUser(userId).getCreated();

        long totalCost = spendRepo.findByUserAndDateBetween(userId, start, end.plusDays(1)).stream()
                .map(widgetService::getDto)
                .map(SpendingWidgetDto::getTotalCost)
                .mapToLong(Long::longValue)
                .sum();

        Long monthId = goalWeek.getGoalMonth();
        long weekAmountSum = weekRepo.findByUserAndDate(monthId, start).stream()
                .map(GoalWeek::getAmount)
                .mapToLong(Long::longValue)
                .sum();

        if (userCreated.isAfter(end))
            goalWeek.setState(STATE_FAILED);
        else if (totalCost > weekAmountSum)
            goalWeek.setState(STATE_FAILED);
        else if (end.isBefore(now))
            goalWeek.setState(STATE_ACHEIVED);
        else
            goalWeek.setState(STATE_PROCEEDING);
        weekRepo.save(goalWeek);
        return true;
    }

    /**
     * 월간 목표에 포함된 주간 목표량의 합을 계산합니다.
     * @param goalMonth 월간 목표
     * @return 주간 목표량 합
     */
    private Long getWeekGoalAmountSum(GoalMonth goalMonth) {
        return goalMonth.getWeekIds().stream()
                .map(this::getWeekById)
                .map(GoalWeek::getAmount)
                .mapToLong(Long::longValue).sum();
    }

    /**
     * 사용자가 달성한 월간 목표의 개수를 반환합니다.
     * @param userId 사용자 ID
     * @return 사용자가 달성한 월간 목표의 개수
     */
    public long getMonthAchieve(String userId) {
        return monthRepo.findByUser(userId).stream()
                .map(GoalMonth::getState)
                .filter(n -> n.contains(STATE_ACHEIVED))
                .count();
    }

    /**
     * 사용자가 달성한 주간 목표의 개수를 반환합니다.
     * @param userId 사용자 ID
     * @return 사용자가 달성한 주간 목표의 개수
     */
    public long getWeekAchieve(String userId) {
        return monthRepo.findByUser(userId).stream()
                .map(GoalMonth::getWeekIds)
                .flatMap(List::stream)
                .map(this::getWeekById)
                .filter(weekGoal -> weekGoal.getState().equals(STATE_ACHEIVED))
                .count();
    }
    
    /**
     * 사용자의 모든 목표를 지웁니다.
     * @param userId 사용자 ID
     * @return 성공 여부
     */
    public boolean deleteGoalAll(String userId) {
        List<GoalMonth> goalMonthList = monthRepo.findByUser(userId);
        List<List<Long>> weekIds = monthRepo.findByUser(userId).stream()
                .map(GoalMonth::getWeekIds)
                .collect(Collectors.toList());

        if(goalMonthList == null) {
            return false;
        }
        for (List<Long> n : weekIds) {
            weekRepo.deleteAllById(n);
        }
        for(GoalMonth n : goalMonthList) {
            monthRepo.delete(n);
        }
        return true;
    }
}