package graduation.spendiary.controller;

import graduation.spendiary.domain.goal.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goal/month")
@RequiredArgsConstructor
public class GoalMonthController {
    @Autowired
    private GoalMonthService goalMonthService;

    @GetMapping
    public List<GoalDto> getAll(@AuthenticationPrincipal String userId) {
        return goalMonthService.getAll(userId);
    }

    @GetMapping(value = "/{goalId}")
    public GoalDto getById(@PathVariable Long goalId) {
        return goalMonthService.getById(goalId);
    }

    @PostMapping
    public Message monthAdd(@AuthenticationPrincipal String userId, @RequestBody GoalMonth goalMonth) {
        boolean success = goalMonthService.monthGoal(userId, goalMonth);
        return new Message(
                success ? "생성 완료" : "생성 실패; 이미 존재합니다",
                success
        );
    }


}
