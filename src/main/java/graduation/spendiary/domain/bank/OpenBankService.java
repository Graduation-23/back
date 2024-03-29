package graduation.spendiary.domain.bank;

import graduation.spendiary.exception.NoRefreshTokenException;
import graduation.spendiary.exception.NoSuchContentException;
import graduation.spendiary.exception.OpenBankRequestFailedException;
import graduation.spendiary.security.config.OpenBankConfig;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OpenBankService {
    @Autowired
    private final OpenBankInfoRepository repo;
    @Autowired
    private final OpenBankConfig config;

    private final String OPEN_BANK_URI = "https://testapi.openbanking.or.kr";
    private final String OPEN_BANK_AUTHORIZE_URI = OPEN_BANK_URI + "/oauth/2.0/authorize";
    private final String OPEN_BANK_TOKEN_URI = OPEN_BANK_URI + "/oauth/2.0/token";
    private final String OPEN_BANK_ACCOUNT_LIST_URI = OPEN_BANK_URI + "/v2.0/account/list";
    private final String OPEN_BANK_TRANSACTION_LIST_URI = OPEN_BANK_URI + "/v2.0/account/transaction_list/fin_num";
    private final String OPEN_BANK_UNLINK_URL = OPEN_BANK_URI + "/v2.0/user/unlink";

    private final int MAX_TRANSACTION_REQUEST = 10;

    /**
     * 사용자 ID에 해당하는 금융결제원 인증 사이트 URI를 생성합니다.
     * @param userId 사용자 ID
     * @return 금융결제원 인증 URI
     */
    public String getAuthUrl(String userId) {
        return UriComponentsBuilder.fromUriString(OPEN_BANK_AUTHORIZE_URI)
                .queryParam("response_type", "code")
                .queryParam("client_id", config.getClientId())
                .queryParam("redirect_uri", config.getRedirectUri())
                .queryParam("scope", "login inquiry")
                .queryParam("client_info", userId)
                .queryParam("state","b80BLsfigm9OokPTjy03elbJqRHOfGSY") // todo: state 생성
                .queryParam("auth_type", "0")
                .queryParam("cellphone_cert_yn", "Y")
                .queryParam("authorized_cert_yn", "Y")
                .queryParam("account_hold_auth_yn", "N")
                .queryParam("register_info", "A")
                .encode().build().toUriString();
    }

    /**
     * 사용자가 금융결제원 인증을 완료했는지 확인합니다.
     * @param userId 사용자 ID
     * @return 완료 여부
     */
    public boolean checkAuth(String userId) {
        return repo.findById(userId).isPresent();
    }

    /**
     * 금융결제원에 사용자 인증(회원가입)을 완료합니다.
     * @param userId
     * @param code
     * @param state
     */
    public void register(String userId, String code, String state) {
        // todo: state 유효성 확인

        // token 발급
        Map response = this.requestToken(code);

        // access token, refresh token, 사용자 번호 DB에 저장
        OpenBankInfo info = OpenBankInfo.builder()
                .id(userId)
                .accessToken((String) response.get("access_token"))
                .refreshToken((String) response.get("refresh_token"))
                .fintechNums(Collections.emptyList())
                .userSeqNo((String) response.get("user_seq_no"))
                .build();
        repo.save(info);
    }

    /**
     * 금융결제원에서 탈퇴합니다.
     * 금융결제원과의 연결을 해지하고 관련 정보를 파기합니다.
     * @param userId 사용자 ID
     * @throws NoSuchContentException 이미 연결이 해지되었거나 연결되지 않음
     */
    public void unregister(String userId)
            throws NoSuchContentException
    {
        unlink(userId);
        repo.deleteById(userId);
    }

    /**
     * 금융결제원 사용자 인증 토큰을 발급받습니다.
     * @param code 사용자 인증 과정에서 받은 code
     * @return 발급 받은 토큰 정보가 포함된 Response
     * @throws OpenBankRequestFailedException 사용자 인증 토큰을 발급하는 데 실패함.
     * todo: 오류 코드 읽어서 발급 실패 원인 상세화 및 처리
     */
    private Map requestToken(String code)
        throws OpenBankRequestFailedException
    {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("client_id", config.getClientId());
        body.add("client_secret", config.getSecret());
        body.add("redirect_uri", config.getRedirectUri());
        body.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
        Map response = restTemplate.postForEntity(OPEN_BANK_TOKEN_URI, requestEntity, Map.class).getBody();

        assert response != null;
        checkTokenResponse(response);

        return response;
    }

    /**
     * DB에서 금융결제원 관련 정보를 가져옵니다.
     * @param userId 사용자 ID
     * @return 사용자의 금융결제원 정보
     * @throws NoSuchContentException 사용자 ID에 해당하는 금융결제원 정보를 찾지 못함
     */
    public OpenBankInfo getInfo(String userId)
            throws NoSuchContentException
    {
        Optional<OpenBankInfo> infoOptional = repo.findById(userId);
        if (infoOptional.isEmpty())
            throw new NoSuchContentException();
        return infoOptional.get();
    }

    /**
     * 금융결제원 사용자 토큰을 refresh 합니다.
     * @param userId 사용자 ID
     * @throws NoSuchContentException 사용자 ID에 해당하는 금융결제원 정보를 찾지 못함
     * @throws NoRefreshTokenException 금융결제원 Refresh token 찾지 못함
     */
    public void refreshToken(String userId)
        throws NoSuchContentException, NoRefreshTokenException, OpenBankRequestFailedException
    {
        OpenBankInfo info = this.getInfo(userId);

        if (info.getRefreshToken() == null)
            throw new NoRefreshTokenException();

        // 금융결제원에 token 요청
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", config.getClientId());
        body.add("client_secret", config.getSecret());
        body.add("refresh_token", info.getRefreshToken());
        body.add("scope", "login inquiry");
        body.add("grant_type", "refresh_token");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 전송
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> responseEntity
                = restTemplate.postForEntity(OPEN_BANK_TOKEN_URI, requestEntity, Map.class);

        Map response = responseEntity.getBody();

        assert response != null;
        checkTokenResponse(response);

        // DB에 갱신
        info.setAccessToken((String) response.get("access_token"));
        info.setRefreshToken((String) response.get("refresh_token"));
        repo.save(info);
    }

    /**
     * 금융결제원과의 연결을 해지합니다.
     * @param userId 사용자 ID
     */
    private void unlink(String userId) {
        OpenBankInfo info = this.getInfo(userId);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(info.getAccessToken());

        Map<String, String> body = new HashMap<>();
        body.put("client_use_code", config.getTranId());
        body.put("user_seq_no", info.getUserSeqNo());

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(body, headers);
        Map response = restTemplate.postForEntity(OPEN_BANK_UNLINK_URL, requestEntity, Map.class).getBody();
        restTemplate.postForObject(OPEN_BANK_UNLINK_URL, requestEntity, Map.class);

        checkResponse(response);
    }

    /**
     * 금융결제원으로부터 등록된 계좌를 조회하여 DB에 저장합니다.
     * @param userId 사용자 ID
     * @throws OpenBankRequestFailedException 금융결제원 처리 과정 중 오류 발생
     */
    public void inquiryAccount(String userId)
        throws OpenBankRequestFailedException
    {
        OpenBankInfo info = this.getInfo(userId);

        RestTemplate restTemplate = new RestTemplate();

        String url = UriComponentsBuilder.fromUriString(OPEN_BANK_ACCOUNT_LIST_URI)
                .queryParam("user_seq_no", info.getUserSeqNo())
                .queryParam("include_cancel_yn", "N")
                .queryParam("sort_order", "A")
                .encode().build().toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(info.getAccessToken());

        // 전송
        Map response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), Map.class).getBody();
        checkResponse(response);

        // 핀테크사용번호 리스트 가져오기
        List<Map<String, String>> results = (List<Map<String, String>>) response.get("res_list");
        List<String> fintechNums = results.stream()
                .map(account -> account.get("fintech_use_num"))
                .collect(Collectors.toList());

        // DB에 핀테크사용번호 저장
        info.setFintechNums(fintechNums);
        repo.save(info);
    }

    /**
     * 사용자의 출금 내역을 가져옵니다.
     * @param userId 사용자 ID
     * @param date 출금 내역을 조회할 날짜
     * @return 출금 내역 리스트, 사용자 계좌가 DB에 없다면 빈 리스트
     * @throws OpenBankRequestFailedException 금융결제원에 요청 실패
     */
    public List<Transaction> getWithdrawTransactionAt(String userId, LocalDate date)
        throws OpenBankRequestFailedException
    {
        OpenBankInfo info;
        try {
            info = this.getInfo(userId);
        }
        catch (NoSuchContentException e) {
            return Collections.emptyList();
        }
        // info에 정보가 없으면 금결원에서 불러옴
        if (info.getFintechNums().isEmpty()) {
            this.inquiryAccount(userId);
            info = this.getInfo(userId);

        }
        RestTemplate restTemplate = new RestTemplate();

        String dateFormatted = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String nowFormatted = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        String baseUrl = UriComponentsBuilder.fromUriString(OPEN_BANK_TRANSACTION_LIST_URI)
                .queryParam("inquiry_type", "O")
                .queryParam("inquiry_base", "D")
                .queryParam("from_date", dateFormatted) // not working
                .queryParam("to_date", dateFormatted) // not working
                .queryParam("sort_order", "A")
                .queryParam("tran_dtime", nowFormatted)
                .encode().build().toUriString();

        // 각 핀테크이용번호로 거래내역을 가져와 합침
//        List<Transaction> result = new ArrayList<>();
        List<Map> responses = new ArrayList<>();

        for (String fintechNum: info.getFintechNums()) {
            String url = UriComponentsBuilder.fromUriString(baseUrl)
                    .queryParam("bank_tran_id", this.generateBankTranId())
                    .queryParam("fintech_use_num", fintechNum)
                    .encode().build().toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(info.getAccessToken());
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(headers);

            // 전송
            Map response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Map.class).getBody();
            checkResponse(response);
//            result.addAll(this.getTransactionsFromResponse(response));
            responses.add(response);

            for (int i=0; i < MAX_TRANSACTION_REQUEST - 1; i++) {
                if (((String) response.get("befor_inquiry_trace_info")).isEmpty())
                    break;
                url = UriComponentsBuilder.fromUriString(baseUrl)
                        .queryParam("bank_tran_id", this.generateBankTranId())
                        .queryParam("fintech_use_num", fintechNum)
                        .queryParam("befor_inquiry_trace_info", response.get("befor_inquiry_trace_info"))
                        .encode().build().toUriString();
                response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Map.class).getBody();
                checkResponse(response);
//            result.addAll(this.getTransactionsFromResponse(response));
                responses.add(response);
            }
        }
//        return result;
        return responses.stream()
                .map(response -> this.getTransactionsFromResponse(response, dateFormatted))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    /**
     * 거래고유번호를 생성합니다.
     * @link https://developers.kftc.or.kr/dev/doc/open-banking#doclist_3_11
     * @return 생성한 거래고유번호
     */
    private String generateBankTranId() {
        return config.getTranId() + "U" + RandomStringUtils.randomNumeric(9).toUpperCase();
    }

    /**
     * 금융결제원에서 온 Token 응답이 유효한지 체크합니다.
     */
    private void checkTokenResponse(Map response) {
        if (response.containsKey("rsp_code")) {
            String rspCode = (String) response.get("rsp_code");
            String rspMessage = (String) response.get("rsp_message");
            throw new OpenBankRequestFailedException(rspCode, rspMessage);
        }
    }
    /**
     * 금융결제원에서 온 응답이 유효한지 체크합니다.
     * @param response 체크할 응답
     * @throws NullPointerException response == null임
     * @throws OpenBankRequestFailedException 금융결제원 오류 발생
     */
    private void checkResponse(Map response)
        throws NullPointerException, OpenBankRequestFailedException
    {
        if (response == null)
            throw new NullPointerException("Response is null");
        String rspCode = (String) response.get("rsp_code");
        String rspMessage = (String) response.get("rsp_message");
        if (rspCode.equals("A0002"))
            return; // 응답 전문 없음
        if (!rspCode.equals("A0000"))
            throw new OpenBankRequestFailedException(rspCode, rspMessage);
    }

    /**
     * 거래내역조회 응답으로부터 Transaction 객체를 가져옵니다.
     * @param response 거래내역조회 응답
     * @return Transaction 객체
     */
    private List<Transaction> getTransactionsFromResponse(Map response, String dateFormatted) {
        List<Map<String, String>> resList = (List<Map<String, String>>) response.get("res_list");
        return resList.stream()
                .filter(res -> dateFormatted.equals(res.get("tran_date"))) // 임시
                .map(res -> Transaction.builder()
                        .bankName((String) response.get("bank_name"))
                        .amount(Long.parseLong((String) res.get("tran_amt")))
                        .transactionType(res.get("tran_type"))
                        .content(res.get("print_content"))
                        .build()
                )
                .collect(Collectors.toList());
    }
}