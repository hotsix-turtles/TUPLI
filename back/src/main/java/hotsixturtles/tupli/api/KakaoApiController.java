package hotsixturtles.tupli.api;

import hotsixturtles.tupli.dto.payments.KakaoPayApprovalVO;
import hotsixturtles.tupli.dto.response.ErrorResponse;
import hotsixturtles.tupli.service.KakaoPayService;
import hotsixturtles.tupli.service.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class KakaoApiController {

    private final KakaoPayService kakaoPayService;

    private final JwtTokenProvider jwtTokenProvider;
    private final MessageSource messageSource;

    /**
     * 카카오페이 api에 필요한 다양한 값들을 조회[GET]
     * @return api사용에 필요한 값들을 http response body에 넣어서 보냄
     */
    @GetMapping("/kakaoPay")
    public String kakaoPaymentReady() {
        String result = kakaoPayService.kakaoPayReady();
        return result;
    }

    /**
     * 카카오페이 결제 성공시 받는 연락 [POST]
     * @param pg_token
     * @param request
     * @return 결제가 성공했을 경우 받는 pgToken을 이용하여, 결제된 금액등 결제 관련 내용을 (필요하면) 저장하고, http response body에 넣어서 보냄
     */
    @PostMapping("/kakaoPay/success")
    public ResponseEntity kakaoPaySuccess(@RequestParam("pgToken") String pg_token,
                                          @RequestHeader(value = "Authorization") String token,
                                          HttpServletRequest request) {
        // 나중에 변조시 필요 일단 보관(ex - headers: {Authorization : 'JWT xasldkjaklsd'})
        // String token = request.getHeader("Authorization").replaceFirst("JWT ", "");

        System.out.println("pg_token = " + pg_token);
        System.out.println("token = " + token);

        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }

        try {
            KakaoPayApprovalVO kakaoPayApprovalVO = kakaoPayService.kakaoPaySuccess(pg_token, token);
            return ResponseEntity.ok().body(kakaoPayApprovalVO);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}
