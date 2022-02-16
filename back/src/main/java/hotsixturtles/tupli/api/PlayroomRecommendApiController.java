package hotsixturtles.tupli.api;

import hotsixturtles.tupli.dto.PlayroomDto;
import hotsixturtles.tupli.dto.response.ErrorResponse;
import hotsixturtles.tupli.entity.Playroom;
import hotsixturtles.tupli.service.PlayroomRecommendService;
import hotsixturtles.tupli.service.token.JwtTokenProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Api(tags = "플레이룸 추천 관련 API")
public class PlayroomRecommendApiController {

    private final PlayroomRecommendService playroomRecommendService;

    private final JwtTokenProvider jwtTokenProvider;

    private final MessageSource messageSource;

    /**
     * 자신이 팔로우한 사용자가 만든 플레이룸을 추천
     * @param token
     * @return playroomList
     * 반환 코드 : 200, 404
     */
    // 2번
    @PostMapping("/recommend/playroomMake")
    @ApiOperation(value = "자신을 팔로우한 사용자가 만든 플레이룸을 추천합니다.", notes = "")
    public ResponseEntity<?> findFolloweesMakePlayroom(@RequestHeader(value = "Authorization") String token){

        Long userSeq = jwtTokenProvider.getUserSeq(token);

        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.
                            getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }

        List<Playroom> playrooms = playroomRecommendService.findFolloweesMakePlayroom(userSeq);
        List<Playroom> additionalPlayrooms = new ArrayList<Playroom>();
        if(playrooms.size() < 5){
            additionalPlayrooms = playroomRecommendService.findPopularLikePlayroom(5);
        }
        int index = 0;
        if(additionalPlayrooms.size() < 5)
            while (playrooms.size() < 5 && index < additionalPlayrooms.size()) {
                if (!playrooms.contains(additionalPlayrooms.get(index))) {
                    playrooms.add(additionalPlayrooms.get(index));
                }
                index++;
            }
        else {
            while (playrooms.size() < 5) {

                if (!playrooms.contains(additionalPlayrooms.get(index))) {
                    playrooms.add(additionalPlayrooms.get(index));
                }
                index++;
            }
        }

        List<PlayroomDto> result = playrooms.stream().map(b -> new PlayroomDto(b)).collect(Collectors.toList());

        return ResponseEntity.ok().body(result);
    }


    /**
     * 자신이 팔로우한 사용자가 좋아요를 누른 플레이룸을 추천
     * @param token
     * @return playroomList
     * 반환 코드 : 200, 404
     */
    // 1번
    @PostMapping("/recommend/playroomLike")
    @ApiOperation(value = "자신이 팔로우한 사용자가 좋아요를 누른 플레이룸을 추천합니다.", notes = "")
    public ResponseEntity<?> findFollowersLikePlayroom(@RequestHeader(value = "Authorization") String token){
        Long userSeq = jwtTokenProvider.getUserSeq(token);

        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.
                            getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }

        List<Playroom> playrooms = playroomRecommendService.findFolloweesLikePlayroom(userSeq);

        List<Playroom> additionalPlayrooms = new ArrayList<Playroom>();
        if(playrooms.size() < 5){
            additionalPlayrooms = playroomRecommendService.findPopularLikePlayroom(5);
        }
        int index = 0;
        if(additionalPlayrooms.size() < 5)
            while (playrooms.size() < 5 && index < additionalPlayrooms.size()) {
                if (!playrooms.contains(additionalPlayrooms.get(index))) {
                    playrooms.add(additionalPlayrooms.get(index));
                }
                index++;
            }
        else {
            while (playrooms.size() < 5) {

                if (!playrooms.contains(additionalPlayrooms.get(index))) {
                    playrooms.add(additionalPlayrooms.get(index));
                }
                index++;
            }
        }

        List<PlayroomDto> result = playrooms.stream().map(b -> new PlayroomDto(b)).collect(Collectors.toList());

        return ResponseEntity.ok().body(result);
    }

    /**
     * 비로그인 or 팔로우 X 유저 플레이룸 시청자순으로 플레이룸 5개 추천
     * @param
     * @return playroomList
     * 반환 코드 : 200, 404
     */
    // 4번
    @GetMapping("/recommend/playroomPopular")
    @ApiOperation(value = "플레이룸 인기순, 최신순으로 5개를 리턴", notes = "")
    public ResponseEntity<?> findPopularLikePlayroom(){
        List<Playroom> playrooms = playroomRecommendService.findPopularLikePlayroom(5);
        List<PlayroomDto> result = playrooms.stream().map(b -> new PlayroomDto(b)).collect(Collectors.toList());

        return ResponseEntity.ok().body(result);
    }


    /**
     * 현재 시간 기준 일주일 전 플레이 룸, (시간 이용 정렬 가능)
     * @param
     * @return playroomList
     * 반환 코드 : 200, 404
     */
    // ??번 맘대루 만듬 일주일 내 인기순으로 한번 건드려봄
    @GetMapping("/recommend/playroomDate")
    @ApiOperation(value = "플레이룸 현재로부터 1주일 내를 기준으로 인기순으로 5개를 리턴", notes = "")
    public ResponseEntity<?> findPopularLikePlaylist(){
        List<Playroom> playrooms = playroomRecommendService.findPopularLikePlayroomDate(5);
        List<PlayroomDto> result = playrooms.stream().map(b -> new PlayroomDto(b)).collect(Collectors.toList());

        return ResponseEntity.ok().body(result);
    }

}
