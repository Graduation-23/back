package graduation.spendiary.controller;

import graduation.spendiary.domain.finance.Finance;
import graduation.spendiary.domain.finance.FinanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
public class FinanceController {

    @Autowired
    private FinanceService financeService;

    @GetMapping
    public List<Finance> getAll(
            @AuthenticationPrincipal String userId
    ) {
        return financeService.getAllOfUser(userId);
    }

    @GetMapping("/{financeId}")
    public Finance finance(@PathVariable Long financeId) {
        return financeService.getById(financeId);
    }

    @PostMapping("/add")
    public Long addFinance(
            @AuthenticationPrincipal String userId,
            @RequestBody Finance finance
    ) {
        return financeService.save(finance, userId);
    }
}
