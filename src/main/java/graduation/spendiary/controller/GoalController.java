package graduation.spendiary.controller;

import graduation.spendiary.domain.goal.Goal;
import graduation.spendiary.domain.goal.GoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
