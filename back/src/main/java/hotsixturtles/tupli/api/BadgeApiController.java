package hotsixturtles.tupli.api;

import hotsixturtles.tupli.dto.response.ErrorResponse;
import hotsixturtles.tupli.entity.UserBadge;
import hotsixturtles.tupli.service.BadgeService;
import hotsixturtles.tupli.service.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BadgeApiController {

    private final BadgeService badgeService;

    private final JwtTokenProvider jwtTokenProvider;

    private final MessageSource messageSource;

    // 어떤 것이 들어오고 나가는지에 대한 정보가 필요.
    // 들어와야 할 PARAMETER : 비교할 column (인덱스 혹은 이름), 해당 column 값
    // 일단 인덱스(badgeSeq) 받는걸로 구현
    // 이 Controller는 Admin 기준에서의 뱃지 변경 예상

//    // 쓸일 없을듯??
//    @PostMapping("/badge/add")
//    public void addBadge(Map<String, String> badgeInfo){
//        List<Badge> badgeList = badgeService.addBadge(badgeInfo);
//
//        // 모든 뱃지 다 가져오기
//        // 값 비교
//    }

    // Badge List
    @GetMapping("/badge/list")
    public ResponseEntity<?> getBadgeList(@RequestHeader(value = "Authorization") String token){
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }

        Long userSeq = jwtTokenProvider.getUserSeq(token);

        List<UserBadge> badgeList = badgeService.getBadgeList(userSeq);

        return ResponseEntity.status(HttpStatus.OK).body(badgeList);
    }

    // POSTMAN 테스트 용
    @GetMapping("/badge/listtest")
    public ResponseEntity<?> getBadgeListTest(@RequestParam("userSeq") Long userSeq){

        List<UserBadge> updateBadges = badgeService.updateBadge(userSeq);

        List<UserBadge> badgeList = badgeService.getBadgeList(userSeq);

        return ResponseEntity.status(HttpStatus.OK).body(badgeList);
    }

//    // Badge Update
//    @PutMapping("/badge/update")
//    public ResponseEntity<?> updateBadge(Map<String,String> userInfo){
//        List<Badge> badgeList = badgeService.updateBadge(userInfo);
//
//        return ResponseEntity.status(HttpStatus.OK).body(badgeList);
//    }

}
