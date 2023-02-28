package graduation.spendiary.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 날짜와 관련된 유틸리티 클래스입니다.
 * @author 구본웅
 */
public class DateUtil {
    // 객체 생성 방지
    private DateUtil() {}

    /**
     * 주어진 날짜가 (목표 상에서) 그 달의 몇 주차인지 구합니다.
     * (1일~그 달 첫번째 일요일: 1주차, 그 달 마지막 )
     * @param date
     * @return
     */
    public static int getWeek(LocalDate date) {
        return Math.min((date.getDayOfMonth() + 6) / 7, 4);
    }

    /**
     * 연, 월, 주차에 해당하는 날짜의 시간과 끝을 가져옵니다.
     * @param year 연
     * @param month 월
     * @param week 주차(1~4)
     * @see DateUtil#getWeek(LocalDate) 
     * @return 날짜의 시간과 끝을 가지고 있는 객체
     */
    public static LocalDatePeriod getDatePeriod(int year, int month, int week) {
        if (week < 1 || 4 < week)
            return null;
        LocalDate start = LocalDate.of(year, month, 1 + (week - 1) * 7);
        LocalDate end;
        if (week == 4)
            end = LocalDate.of(year, month + 1, 1).minusDays(1);
        else
            end = LocalDate.of(year, month, 7 + (week - 1) * 7);
        return new LocalDatePeriod(start, end);
    }

}
