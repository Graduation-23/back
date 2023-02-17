package graduation.spendiary.security.openbank;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class OpenBankUtil {
    /**
     * 은행 거래 고유번호 랜덤 생성
     */
    public String getRandomNum(String bank_tran_id) {
        Random rand = new Random();
        String str = Integer.toString(rand.nextInt(8) + 1);
        for(int i = 0; i < 8; i++) str += Integer.toString(rand.nextInt(9));
        return bank_tran_id + str;
    }
}