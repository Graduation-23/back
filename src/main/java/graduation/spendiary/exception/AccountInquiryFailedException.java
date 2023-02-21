package graduation.spendiary.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "오픈뱅킹 계좌 조회 실패")
public class AccountInquiryFailedException extends RuntimeException {
}
