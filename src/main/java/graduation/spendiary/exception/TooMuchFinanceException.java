package graduation.spendiary.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.TOO_MANY_REQUESTS, reason = "더 이상 금융 정보를 생성할 수 없음")
public class TooMuchFinanceException extends RuntimeException {
}
