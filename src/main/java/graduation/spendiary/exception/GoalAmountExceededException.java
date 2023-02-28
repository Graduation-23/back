package graduation.spendiary.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "주간 목표치의 합이 월간 목표치를 넘음")
public class GoalAmountExceededException extends RuntimeException {
}
