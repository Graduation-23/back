package graduation.spendiary.controller;

import graduation.spendiary.domain.achieve.Achieve;
import graduation.spendiary.domain.achieve.AchieveService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/achieve")
@RequiredArgsConstructor
public class AchieveController {

    @Autowired
    private AchieveService achieveService;

    @GetMapping
    public List<Achieve> getAll(@AuthenticationPrincipal String userId) {
        return achieveService.getAllAchieve(userId);
    }

    @GetMapping("/month")
    public long monthAchieve(@AuthenticationPrincipal String userId){
        return achieveService.getMonthAchieve(userId);
    }
}
