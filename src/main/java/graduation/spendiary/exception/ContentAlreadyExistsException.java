package graduation.spendiary.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "날짜가 중복되는 다이어리를 생성할 수 없음")
public class ContentAlreadyExistsException extends RuntimeException{
}
