package graduation.spendiary;

import graduation.spendiary.util.DateUtil;
import graduation.spendiary.util.LocalDatePeriod;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpendiaryApplicationTests {
    @Test
    public void test1() {
        LocalDatePeriod period = DateUtil.getDatePeriod(2023, 2, 4);
        System.out.println(period.getStart() + " " + period.getEnd());
    }
}
