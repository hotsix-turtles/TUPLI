package hotsixturtles.tupli.api;

import hotsixturtles.tupli.config.auth.AppProperties;
import hotsixturtles.tupli.dto.auth.AuthReqModel;
import hotsixturtles.tupli.dto.response.ApiResponse;
import hotsixturtles.tupli.dto.response.ErrorResponse;
import hotsixturtles.tupli.entity.Badge;
import hotsixturtles.tupli.entity.UserBadge;
import hotsixturtles.tupli.entity.auth.UserPrincipal;
import hotsixturtles.tupli.service.BadgeService;
import hotsixturtles.tupli.service.token.AuthToken;
import hotsixturtles.tupli.service.token.AuthTokenProvider;
import hotsixturtles.tupli.service.token.JwtTokenProvider;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/accounts/auth")
@RequiredArgsConstructor
@Api(tags = "Auth 회원가입 API")
public class AuthApiController {

    private final AppProperties appProperties;
    private final AuthTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final MessageSource messageSource;
    private final BadgeService badgeService;

    @PostMapping("/login")
    public ApiResponse login(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody AuthReqModel authReqModel
    ) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authReqModel.getId(),
                        authReqModel.getPassword()
                )
        );

        String userId = authReqModel.getId();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Date now = new Date();
        AuthToken accessToken = tokenProvider.createAuthToken(
                userId,
                ((UserPrincipal) authentication.getPrincipal()).getRoleType().getCode(),
                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );

        // jwt 토큰 발급
        return ApiResponse.success("token", accessToken.getToken());
    }

    /**
     * Ouath 로그인, 출석체크 뱃지 체크
     * @param token
     * @return
     */
    @PutMapping("/login/success")
    public ResponseEntity<?> getLoginBadge(@RequestHeader(value = "Authorization") String token){

        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }

        Long userSeq = jwtTokenProvider.getUserSeq(token);

        List<UserBadge> userbadges = badgeService.getBadgeList(userSeq);
        List<Long> badges = badgeService.getUserBadgeSeq(userbadges);
        // jwt 토큰 발급
        List<Badge> badgeResult = new ArrayList<>();
        badgeResult = badgeService.checkLoginNum(userSeq, badges);
        badgeResult.addAll(badgeService.checkDaily(userSeq, badges));

        if(badgeResult == null || badgeResult.size() == 0) return ResponseEntity.ok(null);

        return ResponseEntity.ok(badgeResult);

    }

}