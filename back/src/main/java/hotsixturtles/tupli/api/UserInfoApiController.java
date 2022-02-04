package hotsixturtles.tupli.api;


import hotsixturtles.tupli.entity.meta.UserInfo;
import hotsixturtles.tupli.repository.UserInfoRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = "프로필 정보 API")
public class UserInfoApiController {

    private final UserInfoRepository userInfoRepository;

    /**
     * 타인의 정보 찾기
     * @param userSeq
     * @return
     * 반환 코드 : 200, 204, 404
     */
    @GetMapping("/userinfo/{userSeq}")
    @ApiOperation(value = "유저의 프로필 정보 확인", notes = "uid 저장된 정보가 없을 시 204 반환, 성공 시 200[userInfo 값] 반환")
    public ResponseEntity findUserInfo(@ApiParam(value = "path 로 uid 전달받는다.") @PathVariable("userSeq") Long userSeq) {

        UserInfo userInfo = userInfoRepository.findOneByUserSeq(userSeq);
        if(userInfo == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        return ResponseEntity.ok().body(userInfo);
    }




}
