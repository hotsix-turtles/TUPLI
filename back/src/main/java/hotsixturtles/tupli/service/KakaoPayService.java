package hotsixturtles.tupli.service;


import hotsixturtles.tupli.dto.payments.KakaoPayApprovalVO;
import hotsixturtles.tupli.dto.payments.KakaoPayReadyVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoPayService {

    private final UserService userService;

    private static final String HOST = "https://kapi.kakao.com";
    // 카카오페이 내 어플리케이션 플랫폼에서 주소 관리
    @Value("${info.server.front}")
    String FRONTSERVER;  // private static final String FRONTSERVER = "http://localhost:8081";

    @Value("${key.kakao.admin}")
    String ADMIN_KEY;

    private KakaoPayReadyVO kakaoPayReadyVO;
    private KakaoPayApprovalVO kakaoPayApprovalVO;

    public String kakaoPayReady() {

        log.info("KakaoAK {}", ADMIN_KEY);

        RestTemplate restTemplate = new RestTemplate();

        // 서버로 요청할 Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + ADMIN_KEY);  // admin 키
        headers.add("Accept", MediaType.APPLICATION_JSON_UTF8_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");

        // 서버로 요청할 Body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("cid", "TC0ONETIME");
        params.add("partner_order_id", "1001");
        params.add("partner_user_id", "TUPLI");
        params.add("item_name", "Premium");
        params.add("quantity", "1");
        params.add("total_amount", "3300");
        params.add("tax_free_amount", "300");

        // 백엔드 쪽 리턴 주소 설정
        params.add("approval_url", FRONTSERVER + "/kakaoPay/success");  // 모든 절차가 끝나면 저기로 보냄. 체크
        params.add("cancel_url", FRONTSERVER + "/kakaoPay/cancel");  // 결제 취소시 주소
        params.add("fail_url", FRONTSERVER + "/kakaoPay/fail");  // 결제 실패시 주소 (mock 환경이라 생각보다 잘 발생)

        // 헤더 + 바디
        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<MultiValueMap<String, String>>(params, headers);

        // 받아서 넣기
        try {
            kakaoPayReadyVO = restTemplate.postForObject(new URI(HOST + "/v1/payment/ready"), body, KakaoPayReadyVO.class);

            return kakaoPayReadyVO.getNext_redirect_pc_url();

        } catch (RestClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "주소 생성 에러 발생";
    }

    public KakaoPayApprovalVO kakaoPaySuccess(String pg_token, String token) {

        RestTemplate restTemplate = new RestTemplate();

        // 서버로 요청할 Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + ADMIN_KEY);  // admin 키
        headers.add("Accept", MediaType.APPLICATION_JSON_UTF8_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");

        // 서버로 요청할 Body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("cid", "TC0ONETIME");
        params.add("tid", kakaoPayReadyVO.getTid());
        params.add("partner_order_id", "1001");
        params.add("partner_user_id", "TUPLI");
        params.add("pg_token", pg_token);
        params.add("total_amount", "3300");

        // 헤더 + 바디
        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<MultiValueMap<String, String>>(params, headers);

        try {
             kakaoPayApprovalVO = restTemplate.postForObject(new URI(HOST + "/v1/payment/approve"), body, KakaoPayApprovalVO.class);
             userService.rankUpPremium(token);

            log.info("kakaoPayApprovalVO = {}", kakaoPayApprovalVO);

            return kakaoPayApprovalVO;

        } catch (RestClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

}

