package graduation.spendiary.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 날짜 간격을 나타내는 객체입니다. 시작일과 말일을 가지고 있습니다.
 */
@AllArgsConstructor
@Getter
public class LocalDatePeriod {
    LocalDate start;
    LocalDate end;
}
