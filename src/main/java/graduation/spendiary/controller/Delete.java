package graduation.spendiary.controller;

import graduation.spendiary.domain.diary.DiaryService;
import graduation.spendiary.domain.finance.FinanceService;
import graduation.spendiary.domain.goal.GoalService;
import graduation.spendiary.domain.spendingWidget.SpendingWidgetService;
import graduation.spendiary.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/delete")
public class Delete {

    @Autowired
    private UserService userService;

    @Autowired
    private DiaryService diaryService;

    @Autowired
    private FinanceService financeService;

    @Autowired
    private GoalService goalService;

    @Autowired
    private SpendingWidgetService spendingWidgetService;

    @DeleteMapping
    public Message deleteInfo(@AuthenticationPrincipal String userId, String password) {
        boolean user = userService.deleteUser(userId, password);
        boolean diary = diaryService.deleteDiaryAll(userId);
        boolean finance = financeService.deleteFinanceAll(userId);
        boolean goal = goalService.deleteGoalAll(userId);
        boolean spendingWidget = spendingWidgetService.deleteSpendingWidgetAll(userId);
        if(user&&diary&&finance&&goal&&spendingWidget) {
            return new Message("모든 정보 삭제 완료", true);
        }
        return new Message("문제가 있따 머지", false);
    }
}
