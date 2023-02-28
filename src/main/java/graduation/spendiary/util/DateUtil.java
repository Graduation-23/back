package graduation.spendiary.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

/**
 * 날짜와 관련된 유틸리티 클래스입니다.
 * @author 구본웅
 */
public class DateUtil {
    // 객체 생성 방지
    private DateUtil() {}

    /**
     * 주어진 연, 월의 1일의 요일을 숫자로 반환합니다.
     * @param year 연
     * @param month 월
     * @return 월요일이면 1, 화요일이면 2, ..., 일요일이면 7
     */
    public static int getDayOfMonth1st(int year, int month) {
        return LocalDate.of(year, month, 1).getDayOfWeek().getValue();
    }

    /**
     * 주어진 날짜가 (목표 상에서) 그 달의 몇 주차인지 구합니다.
     * (1일~그 달 첫번째 일요일: 1주차, 이후 월~일 주기로 주차, 4주차 이후는 4주차로 편입)
     * @param date
     * @return
     */
    public static int getWeek(LocalDate date) {
        int DayOfMonth1st = DateUtil.getDayOfMonth1st(date.getYear(), date.getMonthValue());

        return Math.min((date.getDayOfMonth() + DayOfMonth1st - 2) / 7 + 1, 4);
    }

    /**
     * 연, 월, 주차에 해당하는 날짜의 월요일(시작)과 일요일(끝)을 가져옵니다.
     * @param year 연
     * @param month 월
     * @param week 주차(1~4)
     * @see DateUtil#getWeek(LocalDate) 
     * @return 날짜의 시간(월요일)과 끝(일요일)을 가지고 있는 객체, 주차가 잘못됐다면 null.
     */
    public static LocalDatePeriod getDatePeriod(int year, int month, int week) {
        if (week < 1 || 4 < week)
            return null;
        int DayOfMonth1st = DateUtil.getDayOfMonth1st(year, month);
        LocalDate start, end;
        start = LocalDate.of(year, month, Math.max(2 - DayOfMonth1st + 7 * (week - 1), 1));
        if (week == 4)
            end = LocalDate.of(year, month + 1, 1).minusDays(1); // 해당월 말일
        else
            end = LocalDate.of(year, month, 8 - DayOfMonth1st + 7 * (week - 1));
        return new LocalDatePeriod(start, end);
    }

}
