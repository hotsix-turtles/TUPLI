package hotsixturtles.tupli.api;

import hotsixturtles.tupli.dto.payments.KakaoPayApprovalVO;
import hotsixturtles.tupli.dto.response.ErrorResponse;
import hotsixturtles.tupli.service.KakaoPayService;
import hotsixturtles.tupli.service.token.JwtTokenProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@Api(tags = "카카오페이 관련 API")
public class KakaoApiController {

    private final KakaoPayService kakaoPayService;

    private final JwtTokenProvider jwtTokenProvider;
    private final MessageSource messageSource;

    /**
     * 카카오페이 api에 필요한 다양한 값들을 조회[GET]
     * @return api사용에 필요한 값들을 http response body에 넣어서 보냄
     */
    @GetMapping("/kakaoPay")
    @ApiOperation(value = "카카오페이 API에 필요한 값들을 조회합니다.", notes = "api사용에 필요한 값들을 http response body에 넣어서 보냄")
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
    @ApiOperation(value = "카카오페이 결제 성공 시 샐행합니다.",
            notes = "결제가 성공했을 경우 받는 pgToken을 이용하여, 결제된 금액등 결제 관련 내용을 (필요하면) 저장하고, http response body에 넣어서 보냄")
    public ResponseEntity kakaoPaySuccess(@RequestParam("pgToken") String pg_token,
                                          @RequestHeader(value = "Authorization") String token,
                                          HttpServletRequest request) {
        // 나중에 변조시 필요 일단 보관(ex - headers: {Authorization : 'JWT xasldkjaklsd'})
        // String token = request.getHeader("Authorization").replaceFirst("JWT ", "");
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
