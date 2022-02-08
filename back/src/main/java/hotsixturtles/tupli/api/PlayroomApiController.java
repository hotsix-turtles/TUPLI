package hotsixturtles.tupli.api;

import hotsixturtles.tupli.dto.PlaylistDto;
import hotsixturtles.tupli.dto.PlayroomDto;
import hotsixturtles.tupli.dto.request.RequestPlayroomDto;
import hotsixturtles.tupli.dto.response.ErrorResponse;
import hotsixturtles.tupli.dto.response.ResponsePlayroomDto;
import hotsixturtles.tupli.entity.Playroom;
import hotsixturtles.tupli.entity.likes.PlayroomLikes;
import hotsixturtles.tupli.service.PlayroomService;
import hotsixturtles.tupli.service.token.JwtTokenProvider;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PlayroomApiController {

    private final PlayroomService playroomService;

    private final JwtTokenProvider jwtTokenProvider;

    private final MessageSource messageSource;

    /**
     * 플레이룸 리스트 출력
     * @return
     * 반환 코드 : 200, 404
     */
    @GetMapping("/playroom/list")
    public ResponseEntity<?> getPlayroomList(){

        List<Playroom> playroomList = playroomService.getPlayroomList();

        List<PlayroomDto> result = playroomList.stream().map(b -> new PlayroomDto(b)).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * 플레이룸 정보 가져오기
     * @param playroomId
     * @return
     * 반환 코드: 200, 404
     */

    @GetMapping("/playroom/{playroomId}")
    public ResponseEntity<?> getPlayroom(@PathVariable("playroomId") Long playroomId){

        Playroom playroom = playroomService.getPlayroom(playroomId);
        if(playroom == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new PlayroomDto(playroom));
    }

    /**
     * 플레이룸 추가
     * @param token
     * @param playroomDto : {roomTitle, roomContent, roomPublic,roominviteIds,
     *                        roomPlaylists, roomTags, roomVideos, startTime, roomEndTime}
     * @return
     * 반환 코드 : 201, 403, 404
     */
    @PostMapping("/playroom")
    public ResponseEntity<?> addPlayroom(@RequestHeader(value = "Authorization") String token,
                                         @RequestBody RequestPlayroomDto playroomDto){
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);

        PlayroomDto playroomResult = playroomService.addPlayroom(playroomDto, userSeq);

        return ResponseEntity.status(HttpStatus.CREATED).body(playroomResult);
    }

    @PutMapping("/playroom/{playroomId}/user")
    public ResponseEntity changePlayroomUser(@RequestHeader(value = "Authorization") String token,
                                             @PathVariable("playroomId") Long playroomId) {

        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);

        playroomService.changePlayroomUser(userSeq, playroomId);

        return ResponseEntity.ok().body(null);
    }

    /**
     *
     * @param token
     * @param playroomId
     * @param playroomDto  : {roomTitle, roomContent} // 다른 값도 추가 가능
     * @return
     * 반환 코드 : 200, 401, 403, 404
     */
    @PutMapping("/playroom/{playroomId}")
    public ResponseEntity<?> updatePlayroom(@RequestHeader(value = "Authorization") String token,
                                            @PathVariable("playroomId") Long playroomId,
                                            @RequestBody RequestPlayroomDto playroomDto){

        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }

        Long userSeq = jwtTokenProvider.getUserSeq(token);

        PlayroomDto result = playroomService.updatePlayroom(playroomId, playroomDto, userSeq);

        if(result == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     *
     * @param token
     * @param playroomId
     * @return
     * 반환코드 : 200, 401, 403, 404
     */
    @DeleteMapping("/playroom/{playroomId}")
    public ResponseEntity<?> deletePlayroom(@RequestHeader(value = "Authorization") String token,
                                            @PathVariable("playroomId") Long playroomId){

        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }

        Long userSeq = jwtTokenProvider.getUserSeq(token);

        Playroom result = playroomService.deletePlayroom(playroomId, userSeq);

        if(result == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /**
     * 사용자가 좋아요한 플레이룸
     * @param token
     * @return
     * * 반환코드 : 200, 403, 404
     */
    @GetMapping("/playroom/likes")
    public ResponseEntity<?> getPlayroomLiked(@RequestHeader(value = "Authorization") String token)
    {
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);

        List<Playroom> playrooms = playroomService.getLikedPlayrooms(userSeq);

        List<PlayroomDto> result = playrooms.stream().map(b -> new PlayroomDto(b)).collect(Collectors.toList());

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/playroom/{playroomId}/like")
    @ApiOperation(value = "플레이룸 좋아요 확인", notes = "유저 정보가 일치하지 않으면 404, '유효하지 않은 토큰입니다' 반환," +
            "정상 등록 시 200, null 반환")
    public ResponseEntity<?> getBoardLike(@ApiParam(value = "auth token")
                                          @RequestHeader(value = "Authorization") String token,
                                          @ApiParam(value = "플레이룸 id") @PathVariable("playroomId") Long playroomId) {
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.
                            getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);
        PlayroomLikes playroomLikes = playroomService.getPlayroomLike(userSeq, playroomId);
        return ResponseEntity.status(HttpStatus.OK).body(playroomLikes);
    }

    @PostMapping("/playroom/{playroomId}/like")
    @ApiOperation(value = "플레이룸에 좋아요 등록", notes = "유저 정보가 일치하지 않으면 404, '유효하지 않은 토큰입니다' 반환," +
            "정상 등록 시 200, null 반환")
    public ResponseEntity<?> addBoardLike(@ApiParam(value = "auth token")
                                          @RequestHeader(value = "Authorization") String token,
                                          @ApiParam(value = "플레이룸 id") @PathVariable("playroomId") Long playroomId) {
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.
                            getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);
        playroomService.addPlaylistLike(userSeq, playroomId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /**
     * 게시글에 좋아요 취소하기
     * @param token
     * @param playroomId
     * @return
     */
    @DeleteMapping("/playroom/{playroomId}/like")
    @ApiOperation(value = "플레이룸 좋아요 해제", notes = "유저 정보가 일치하지 않으면 404, '유효하지 않은 토큰입니다' 반환," +
            "정상 등록 시 200, null 반환")
    public ResponseEntity<?> deleteBoardLike(@ApiParam(value = "auth token")
                                             @RequestHeader(value = "Authorization") String token,
                                             @ApiParam(value = "플레이룸 id") @PathVariable("playroomId") Long playroomId) {
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.
                            getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);
        playroomService.deletePlayroomLike(userSeq, playroomId);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
