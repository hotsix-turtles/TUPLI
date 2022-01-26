package hotsixturtles.tupli.api;


import hotsixturtles.tupli.entity.UserInfo;
import hotsixturtles.tupli.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserInfoApiController {

    private final UserInfoRepository userInfoRepository;

    /**
     * 타인의 정보 찾기
     * @param userSeq
     * @return
     * 반환 코드 : 200, 204, 404
     */
    @GetMapping("/userinfo/{userSeq}")
    public ResponseEntity findUserInfo(@PathVariable("userSeq") Long userSeq) {

        UserInfo userInfo = userInfoRepository.findOneByUserSeq(userSeq);
        if(userInfo == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.ok().body(userInfo);
    }




}
