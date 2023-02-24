package graduation.spendiary.controller;


import graduation.spendiary.domain.goal.GoalDto;
import graduation.spendiary.domain.goal.GoalWeek;
import graduation.spendiary.domain.goal.GoalWeekService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goal/week")
@RequiredArgsConstructor
public class GoalWeekController {
    @Autowired
    private GoalWeekService goalWeekService;

    @GetMapping
    public List<GoalWeek> getThisGoal(@RequestParam Long goalMonthId) {
        return goalWeekService.getThisGoal(goalMonthId);
    }

    @PostMapping
    public Message weekAdd(@RequestParam Long goalMonthId, @RequestBody GoalWeek goalWeek) {
        boolean success = goalWeekService.weekGoal(goalMonthId, goalWeek);
        return new Message(
                success ? "생성 완료" : "생성 실패; 이미 존재합니다",
                success
        );
    }
}
