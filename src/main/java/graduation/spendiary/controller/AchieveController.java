package graduation.spendiary.controller;

import graduation.spendiary.domain.achieve.Achieve;
import graduation.spendiary.domain.achieve.AchieveService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public Message create(@AuthenticationPrincipal String userId, @RequestBody Achieve achieve) {
        boolean success = achieveService.createAchieve(userId, achieve);
        return new Message(
                success ? "생성 완료" : "생성 실패; 이미 생성하였습니다", success
        );
    }

    @GetMapping("/month")
    public long monthAchieve(@AuthenticationPrincipal String userId){
        return achieveService.getMonthAchieve(userId);
    }

    @GetMapping("/week")
    public long weekAchieve(@AuthenticationPrincipal String userId){
        return achieveService.getWeekAchieve(userId);
    }
}
