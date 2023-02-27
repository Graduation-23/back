package graduation.spendiary.controller;

import graduation.spendiary.domain.goal.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;

@RestController
@RequestMapping("/api/goal")
@RequiredArgsConstructor
public class GoalController {
    @Autowired
    private GoalService goalService;

    @GetMapping("/month")
    public List<GoalMonth> getAll(@AuthenticationPrincipal String userId) {
        return goalService.getAll(userId);
    }

    @GetMapping(value = "/month/{goalId}")
    public GoalMonth getMonthGoalById(@PathVariable Long goalId) {
        return goalService.getMonthById(goalId);
    }

    @GetMapping(value = "/week/{goalId}")
    public GoalWeek getWeekById(@PathVariable Long goalId) {
        return goalService.getWeekById(goalId);
    }

    @PostMapping("/month")
    public Message monthGoalAdd(@AuthenticationPrincipal String userId, @RequestBody GoalMonth goalMonth) {
        boolean success = goalService.monthGoal(userId, goalMonth);
        return new Message(
                success ? "생성 완료" : "생성 실패; 이미 존재합니다",
                success
        );
    }

    @GetMapping("/week")
    public List<GoalWeek> getThisGoal(@RequestParam Long goalMonthId) {
        return goalService.getThisGoal(goalMonthId);
    }

    @PostMapping("/week")
    public Message weekGoalAdd(@RequestParam Long goalMonthId, @RequestBody GoalWeek goalWeek) {
        boolean success = goalService.weekGoal(goalMonthId, goalWeek);
        if(success) goalService.insertWeekId(goalMonthId, goalWeek);
        return new Message(
                success ? "생성 완료" : "생성 실패;", success
        );
    }

    //@Scheduled(cron = "0 10 00 1 *") // 매월 1일 오전 00시 00분에 실행
    @GetMapping("/month/state")
    public Message monthState(@AuthenticationPrincipal String userId, @RequestParam Long monthId){
        goalService.checkMonthState(userId, monthId);
        return new Message("상태 변경", true);
    }
    //@Scheduled(cron = "0 0 0 ? * MON") //매주 월요일마다
    @GetMapping("/week/state")
    public Message weekState(@AuthenticationPrincipal String userId, @RequestParam Long weekId){
        goalService.checkWeekState(userId, weekId);
        return new Message("상태 변경", true);
    }

    @GetMapping("/month/{year}/{month}")
    public GoalMonth getMonthByDate(
            @AuthenticationPrincipal String userId,
            @PathVariable("year") int year,
            @PathVariable("month") int month
    ) {
        return goalService.getGoalMonthOf(userId, year, month);
    }

}
