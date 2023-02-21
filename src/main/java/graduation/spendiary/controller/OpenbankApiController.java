package graduation.spendiary.controller;

import graduation.spendiary.domain.bank.OpenBankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/openbank")
public class OpenbankApiController {
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

    @Autowired
    private final OpenBankService openBankService;

    /**
     * 토큰 발급 요청
     */
    //@GetMapping
    //public String getToken(BankRequestToken bankRequestToken, Model model) {
        //BankResponseToken token = openBankService.requestToken(bankRequestToken);
        //model.addAttribute("bankResponseToken", token);
        //log.info("bankResponseToken={}", token);
        //return "v1/bank";
    //}

    @GetMapping("/auth/uri")
    public ResponseEntity getAuthUri(@AuthenticationPrincipal String userId)
        throws URISyntaxException
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI(openBankService.getAuthUrl(userId)));
        return ResponseEntity.status(HttpStatus.SEE_OTHER).headers(headers).build();
    }

    @GetMapping("/auth")
    public ResponseEntity getAuth(
            @RequestParam("code") String code,
            @RequestParam("client_info") String userId,
            @RequestParam("state") String state
    ) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}