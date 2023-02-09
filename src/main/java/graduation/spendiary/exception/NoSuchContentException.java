package graduation.spendiary.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "주어진 정보로 데이터를 찾을 수 없음")
public class NoSuchContentException extends RuntimeException{
}
