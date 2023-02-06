package graduation.spendiary.controller;

import graduation.spendiary.domain.goal.Goal;
import graduation.spendiary.domain.goal.GoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Goal> getAll() {
        return goalService.getAll();
    }

    @GetMapping("/{goalId}")
    public Goal getById(@PathVariable long goalId) {
        return goalService.getById(goalId);
    }

    @PostMapping
    public Goal add(@RequestBody Goal goal, @AuthenticationPrincipal String userId) {
        return goalService.save(goal, userId);
    }
}
