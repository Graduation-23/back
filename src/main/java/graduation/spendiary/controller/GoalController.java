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
    public Goal monthAdd(@AuthenticationPrincipal String userId, @RequestBody Goal goal) {
        return goalService.monthGoal(userId, goal);
    }

    @PostMapping("/week")
    public Goal weekAdd(@AuthenticationPrincipal String userId, @RequestBody Goal goal) {
        return goalService.weekGoal(userId, goal);
    }


}
