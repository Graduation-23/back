package graduation.spendiary.controller;

import graduation.spendiary.domain.goal.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public GoalMonth getById(@PathVariable Long goalId) {
        return goalService.getMonthById(goalId);
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

    @Scheduled(cron = "0 10 00 1 * ?") // 매월 1일 오전 00시 00분에 실행
    @GetMapping
    public Message monthState(@AuthenticationPrincipal String userId){
        goalService.checkMonthState(userId);
        return new Message("상태 변경", true);
    }

}
