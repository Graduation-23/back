package graduation.spendiary.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "다이어리를 더 이상 수정할 수 없음")
public class NoRefreshTokenException extends RuntimeException{
}
