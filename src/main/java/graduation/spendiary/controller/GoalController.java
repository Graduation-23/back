package graduation.spendiary.controller;

import graduation.spendiary.domain.goal.Goal;
import graduation.spendiary.domain.goal.GoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goal")
@RequiredArgsConstructor
public class GoalController {
    @Autowired
    private GoalService goalService;

    @GetMapping
    public List<Goal> getAll(@AuthenticationPrincipal String userId) {
        return goalService.getAll(userId);
    }

    @GetMapping(value = "/{goalId}")
    public Goal getById(@PathVariable long goalId) {
        return goalService.getById(goalId);
    }

    @PostMapping("/month")
    public Message monthAdd(@AuthenticationPrincipal String userId, @RequestBody Goal goal) {
        boolean success = goalService.monthGoal(userId, goal);
        return new Message(
                success ? "생성 완료" : "생성 실패; 이미 존재합니다",
                success
        );
    }

    @PostMapping("/week")
    public Message weekAdd(@AuthenticationPrincipal String userId, @RequestBody Goal goal) {
        boolean success = goalService.weekGoal(userId, goal);
        return new Message(
                success ? "생성 완료" : "생성 실패; 이미 존재합니다",
                success
        );
    }


}
