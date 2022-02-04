package hotsixturtles.tupli.api;

import hotsixturtles.tupli.dto.simple.SimpleYoutubeVideoDto;
import hotsixturtles.tupli.dto.response.ErrorResponse;
import hotsixturtles.tupli.dto.simple.YoutubeVideoLikesSavedDto;
import hotsixturtles.tupli.entity.youtube.YoutubeVideo;
import hotsixturtles.tupli.service.YoutubeVideoService;
import hotsixturtles.tupli.service.token.JwtTokenProvider;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Api(tags = "유튜브 영상 정보 API")
public class YoutubeVideoApiController {

    private final JwtTokenProvider jwtTokenProvider;

    private final MessageSource messageSource;

    private final YoutubeVideoService youtubeVideoService;

    /**
     * 유튜브 영상 저장하기 >> Input 값이 뭐가 올지 확정할 필요 있음
     * @param token
     * @param youtubeVideoDto
     * @return
     * 반환코드 : 201, 404
     */
    @PostMapping("/video")
    public ResponseEntity<?> saveVideo(@RequestHeader(value = "Authorization") String token,
                                      @RequestBody SimpleYoutubeVideoDto youtubeVideoDto){
        // 유저 정보
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);

        //영상 추가 or 검색 후 영상 저장 (Transaction상 분리, 합치지 말 것)
        YoutubeVideo youtubeVideo = youtubeVideoService.addVideo(youtubeVideoDto);
        youtubeVideoService.saveVideo(userSeq, youtubeVideo);

        // 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    /**
     * 유튜브 영상 저장 취소
     * @param token
     * @param url
     * @return
     * 반환코드 : 204, 404
     */
    @DeleteMapping("/video/{url}")
    public ResponseEntity<?> forgotVideo(@RequestHeader(value = "Authorization") String token,
                                         @PathVariable("url") String url){
        // 유저 정보
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);

        // 저장 영상 관계만 삭제
        youtubeVideoService.forgotVideo(userSeq, url);

        // 반환
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    /**
     * 유튜브 영상 좋아요 >> Input 값이 뭐가 올지 확정할 필요 있음, 예상값은 Body에 담아서 오기
     * @param token
     * @param youtubeVideoDto
     * @return
     * 반환코드 : 201, 404
     */
    @PostMapping("/video/likes")
    public ResponseEntity<?> likesVideo(@RequestHeader(value = "Authorization") String token,
                                       @RequestBody SimpleYoutubeVideoDto youtubeVideoDto){
        // 유저 정보
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);

        //영상 추가 or 검색 후 영상 좋아요 (Transaction상 분리, 합치지 말 것)
        YoutubeVideo youtubeVideo = youtubeVideoService.addVideo(youtubeVideoDto);
        youtubeVideoService.likesVideo(userSeq, youtubeVideo);

        // 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    /**
     * 유튜브 영상 저장 취소
     * @param token
     * @param url
     * @return
     * 반환코드 : 204, 404
     */
    @DeleteMapping("/video/likes/{url}")
    public ResponseEntity<?> dislikesVideo(@RequestHeader(value = "Authorization") String token,
                                         @PathVariable("url") String url){
        // 유저 정보
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);

        // 저장 영상 관계만 삭제
        youtubeVideoService.forgotVideo(userSeq, url);

        // 반환
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }


    /**
     * 해당 유저가 저장한 영상 리스트 전부 반환 (UserapiController로 이동 예정)
     * @param token
     * @return
     * 반환코드 : 200, 404
     */
    @GetMapping("/profile/video/saved")
    public ResponseEntity getAllSavedVideos(@RequestHeader(value = "Authorization") String token) {
        // 유저 정보
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);

        // 해당 유저가 저장한 영상
        List<YoutubeVideo> youtubeVideoList = youtubeVideoService.getAllSavedVideos(userSeq);

        List<SimpleYoutubeVideoDto> result = youtubeVideoList.stream()
                .map(u -> new SimpleYoutubeVideoDto(u))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(result);
    }

    /**
     * 해당 유저가 좋아요한 영상 리스트 전부 반환 (UserapiController로 이동 예정)
     * @param token
     * @return
     * 반환코드 : 200, 404
     */
    @GetMapping("/profile/video/likes")
    public ResponseEntity getAllLikesVideos(@RequestHeader(value = "Authorization") String token) {
        // 유저 정보
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);

        // 해당 유저가 저장한 영상
        List<YoutubeVideo> youtubeVideoList = youtubeVideoService.getAllLikesVideos(userSeq);

        List<SimpleYoutubeVideoDto> result = youtubeVideoList.stream()
                .map(u -> new SimpleYoutubeVideoDto(u))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(result);
    }

    /**
     * 프론트 검색 종료 후, 검색 결과 중에 좋아요, 저장했는지 여부.
     * @param token
     * @param urls
     * @return
     */
    @GetMapping("/profile/video/isLikes")
    public ResponseEntity getSearchResultInfo(@RequestHeader(value = "Authorization") String token,
                                              @RequestPart(value = "urls") List<String> urls) {
        // 유저 정보
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);

        // 정보 담기
        YoutubeVideoLikesSavedDto result = youtubeVideoService.getSearchResultInfo(userSeq, urls);

        return ResponseEntity.ok().body(result);
    }




}
