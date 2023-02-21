package graduation.spendiary.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "오픈뱅킹 인증 토큰 발급 실패")
public class OpenBankTokenFailedException extends RuntimeException{
}
