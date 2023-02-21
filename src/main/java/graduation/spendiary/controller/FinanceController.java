package graduation.spendiary.controller;

import graduation.spendiary.domain.finance.Finance;
import graduation.spendiary.domain.finance.FinanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
public class FinanceController {

    @Autowired
    private FinanceService financeService;

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<Finance> getAll(
            @AuthenticationPrincipal String userId
    ) {
        return financeService.getAllOfUser(userId);
    }

    @GetMapping(value = "/{financeId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Finance finance(@PathVariable Long financeId) {
        return financeService.getById(financeId);
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Long addFinance(
            @AuthenticationPrincipal String userId,
            @RequestBody Finance finance
    ) {
        return financeService.save(finance, userId);
    }

    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Message deleteFinance(@AuthenticationPrincipal String userId, @RequestParam("financeId") Long id) {
        financeService.deleteFinance(userId, id);
        return new Message("삭제 완료", true);
    }
}
