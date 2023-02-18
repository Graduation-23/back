package graduation.spendiary.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/bank")
@RequiredArgsConstructor
public class OpenbankingController {
    // TODO: 2023-02-05
    /*
    1. 사용자인증(oauth/2.0/authorize)
        요청: client_id, redirect_uri
        응답: authorization_code
    2. 사용자토큰발급(oauth/2.0/token)
        요청: authorization_code
        응답: access_token, 사용자일련번호
    <사용자토큰 Headers에 "access_token, Bearer" 입력, 이용기관토큰 X>
    3. 등록계좌조회(account/list)
        요청: access_token, 사용자일련번호
        응답: 핀테크이용번호
    4. 거래내역조회(account/transaction_list/fin_num)
        요청: 은행거래고유번호(M202300194U+9자리난수), 핀테크이용번호
        응답: 거래구분, 거래점명, 거래후잔액, 통장인자내용, 계좌잔액 등
    */
}