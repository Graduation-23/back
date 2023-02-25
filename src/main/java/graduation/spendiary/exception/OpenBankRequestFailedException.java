package graduation.spendiary.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "오픈뱅킹 조회 실패")
@Getter
public class OpenBankRequestFailedException extends RuntimeException {
    public String code;

    public OpenBankRequestFailedException(String code, String msg) {
        super(msg);
        this.code = code;
    }
}
