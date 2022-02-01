package hotsixturtles.tupli.api;

import hotsixturtles.tupli.dto.BoardDto;
import hotsixturtles.tupli.dto.PlaylistDto;
import hotsixturtles.tupli.dto.param.SimpleCondition;
import hotsixturtles.tupli.dto.response.ErrorResponse;
import hotsixturtles.tupli.entity.Board;
import hotsixturtles.tupli.entity.Playlist;
import hotsixturtles.tupli.entity.likes.BoardLikes;
import hotsixturtles.tupli.entity.likes.PlaylistLikes;
import hotsixturtles.tupli.repository.PlaylistRepository;
import hotsixturtles.tupli.service.PlaylistService;
import hotsixturtles.tupli.service.token.JwtTokenProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "플레이 리스트 관련 API")
public class PlaylistApiController {

    private final JwtTokenProvider jwtTokenProvider;
    private final MessageSource messageSource;

    private final PlaylistService playlistService;

    /**
     * 플레이 리스트 추가
     * @param token
     * @param playlist
     * @return 
     * 반환 코드 : 201, 403, 404
     * 고민사항 : ID 또는 PlaylistDto 반환이라도 해줘야 하는지 고민
     */
    @PostMapping("/playlist")
    public ResponseEntity addPlaylist(@RequestHeader(value = "Authorization") String token,
                                      @RequestBody Playlist playlist){
        // 유저 정보
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);

        // 추가 ($$$ try except해서 exception할것인지)
        playlistService.addPlaylist(userSeq, playlist);

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    /**
     * 플레이리스트 단일 GET
     * @param id
     * @return
     * 반환 코드 : 200, 404
     */
    @GetMapping("/playlist/{id}")
    public ResponseEntity getPlaylist(@PathVariable("id") Long id){

        Playlist playlist = playlistService.getPlaylist(id);
        return ResponseEntity.status(HttpStatus.OK).body(new PlaylistDto(playlist));
    }

    /**
     * 플레이리스트 단일 UPDATE
     * @param token
     * @param id
     * @param playlistChange
     * @return
     * 반환 코드 : 200, 404
     */
    @PutMapping("/playlist/{id}")
    public ResponseEntity<?> updatePlaylist(@RequestHeader(value = "Authorization") String token,
                                         @PathVariable("id") Long id,
                                         @RequestBody Playlist playlistChange){
        // 유저 인증
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }

        playlistService.updatePlaylist(id, playlistChange);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /**
     * 플레이리스트 단일 Delete
     * @param token
     * @param id
     * @return
     * 반환 코드 : 204, 404
     */
    @DeleteMapping("/playlist/{id}")
    public ResponseEntity<?> deletePlaylist(@RequestHeader(value = "Authorization") String token, @PathVariable("id") Long id){
        // 유저 인증
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }

        playlistService.deletePlaylist(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    /**
     * 플레이리스트 단일 좋아요 여부
     * @param token
     * @param id
     * @return
     * 반환코드 : 200 204 404
     */
    @GetMapping("/playlist/{id}/like")
    public ResponseEntity getPlaylistLike(@RequestHeader(value = "Authorization") String token,
                                          @PathVariable("id") Long id) {
        // 유저 인증 & 정보
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.getMessage
                            ("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);

        PlaylistLikes playlistLike = playlistService.getPlaylistLike(userSeq, id);
        if (playlistLike == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);  // 안했음
        }
        return ResponseEntity.status(HttpStatus.OK).body("ok");  // 했음
    }


    /**
     * 플레이리스트 단일 좋아요
     * @param token
     * @param id
     * @return
     */
    @PostMapping("/playlist/{id}/like")
    public ResponseEntity<?> addPlaylistLike(@RequestHeader(value = "Authorization") String token,
                                             @PathVariable("id") Long id) {
        // 유저 인증 & 정보
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.
                            getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);

        playlistService.addPlaylistLike(userSeq, id);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }


    /**
     * 플레이리스트 단일 좋아요 취소
     * @param token
     * @param id
     * @return
     */
    @DeleteMapping("/playlist/{id}/like")
    public ResponseEntity<?> deletePlaylistLike(@RequestHeader(value = "Authorization") String token,
                                                @PathVariable("id") Long id) {
        // 유저 인증 & 정보
        if (!jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(messageSource.
                            getMessage("error.valid.jwt", null, LocaleContextHolder.getLocale())));
        }
        Long userSeq = jwtTokenProvider.getUserSeq(token);

        playlistService.deletePlaylistLike(userSeq, id);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    /**
     * 플레이리스트 단순 검색
     * @param condition Simplcondition : [type: {관련순(default), 최신순(), keyword}]
     * @return
     */
    @GetMapping("/playlist/search")
    public ResponseEntity searchPlaylistSimple(SimpleCondition condition) {

        List<Playlist> playlists = playlistService.searchPlaylistSimple(condition);

        List<PlaylistDto> response = playlists.stream().map(x -> new PlaylistDto(x)).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
