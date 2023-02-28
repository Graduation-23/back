package graduation.spendiary.util;

import java.time.LocalDate;

/**
 * 날짜와 관련된 유틸리티 클래스입니다.
 * @author 구본웅
 */
public class DateUtil {
    // 객체 생성 방지
    private DateUtil() {}

    /**
     * 주어진 날짜가 (목표 상에서) 몇 주차인지 구합니다.
     * (1~7일: 1주차, 8~14일: 2주차, ..., 22~말일: 4주차)
     * @param date
     * @return
     */
    public static int getWeek(LocalDate date) {
        return Math.min((date.getDayOfMonth() + 6) / 7, 4);
    }

}
