package graduation.spendiary.controller;

import graduation.spendiary.domain.DatabaseSequence.SequenceGeneratorService;
import graduation.spendiary.domain.finance.Finance;
import graduation.spendiary.domain.finance.FinanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
public class FinanceController {

    @Autowired
    private FinanceService financeService;

    @GetMapping
    public List<Finance> finances(Model model) {
        List<Finance> finances = financeService.getAll();
        model.addAttribute("finances", finances);
        return finances;
    }

    @GetMapping("/{financeId}")
    public Finance finance(@PathVariable Long financeId, Model model) {
        Finance finance = financeService.getById(financeId);
        model.addAttribute("finance", finance);
        return finance;
    }

    @PostMapping("/add")
    public String addFinance(@RequestBody Finance finance) {
        finance.setId(SequenceGeneratorService.generateSequence(Finance.SEQUENCE_NAME));
        financeService.save(finance);
        return "생성";
    }
}
