package hotsixturtles.tupli.api;

import hotsixturtles.tupli.dto.response.BoardResponseDto;
import hotsixturtles.tupli.dto.PlaylistDto;
import hotsixturtles.tupli.dto.PlayroomDto;
import hotsixturtles.tupli.dto.simple.SimpleBadgeDto;
import hotsixturtles.tupli.entity.*;
import hotsixturtles.tupli.service.*;
import hotsixturtles.tupli.service.token.JwtTokenProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "메인페이지 관련 API")
public class HomeApiController {

    private final BoardService boardService;
    private final PlaylistService playlistService;
    private final PlayroomService playroomService;
    private final HomeInfoService homeInfoService;
    private final UserInfoService userInfoService;

    private final JwtTokenProvider jwtTokenProvider;

    // 이 부분은 하영님이 명세 작성해주신대여 그거 나오면 좀 달라질듯
    @GetMapping("/home/board")
    @ApiOperation(value = "메인페이지에 출력될 게시물 목록을 리턴", notes = "")
    public ResponseEntity<?> getHomeBoardList(@PageableDefault(size = 10, sort ="id",  direction = Sort.Direction.DESC) Pageable pageable){
        List<Board> boardList= boardService.getHomeBoardList(pageable);
        if(boardList.size() == 0) return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        List<BoardResponseDto> result = boardList.stream().map(b -> new BoardResponseDto(b)).collect(Collectors.toList());

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/home/playlist")
    @ApiOperation(value = "메인페이지에 출력될 플레이리스트 목록을 리턴", notes = "")
    public ResponseEntity<?> getHomePlayLists(@PageableDefault(size = 10, sort ="id",  direction = Sort.Direction.DESC) Pageable pageable){
            List<Playlist> playlists = playlistService.getHomePlaylists(pageable);
            if(playlists.size() == 0) return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            List<PlaylistDto> result = playlists.stream().map(b -> new PlaylistDto(b)).collect(Collectors.toList());

            return ResponseEntity.ok().body(result);

    }

    @GetMapping("/home/playroom")
    @ApiOperation(value = "메인페이지에 출력될 플레이룸 목록을 리턴", notes = "")
    public ResponseEntity<?> getHomePlayroomList(@PageableDefault(size = 10, sort ="id",  direction = Sort.Direction.DESC) Pageable pageable){
        List<Playroom> playrooms = playroomService.getHomePlayrooms(pageable);
        if(playrooms.size() == 0) return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        List<PlayroomDto> result = playrooms.stream().map(b -> new PlayroomDto(b)).collect(Collectors.toList());

        return ResponseEntity.ok().body(result);
    }

    /**
     * 메인 페이지 검색 ( playroom, playlist, board 상관없이 최신순으로 가져오기 )
     * @param pageable
     * @return
     * 반환 코드 : 200, 204, 404
     */
    @GetMapping("/home/all")
    @ApiOperation(value = "메인페이지에 출력될 플레이룸,플레이리스트,게시글을 종류에 상관없이 최신순으로 리턴", notes = "")
    public ResponseEntity<?> getAllList(@PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                        HttpServletRequest request) {

        // 회원, 비회원(유효하지 않은 토큰) 구분
        String token = request.getHeader("Authorization");
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            List<Object> homeInfos = homeInfoService.getHomeInfoList(-1L, pageable);
            if (homeInfos.size() == 0) return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            return ResponseEntity.ok().body(homeInfos);
        } else {
            Long userSeq = jwtTokenProvider.getUserSeq(token);
            List<Object> homeInfos = homeInfoService.getHomeInfoList(userSeq, pageable);
            if (homeInfos.size() == 0) return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);

            ConcurrentHashMap<String, Integer> tastes = userInfoService.getTaste(userSeq);

//            // 테스트용 코드입니다 시작
//                tastes.put("엔터테인먼트", 3);
//
//            // 테스트용 코드 끝

            if(tastes == null || tastes.size() == 0){
                return ResponseEntity.ok().body(homeInfos);
            }
            homeInfos = homeInfoService.getRecommendHomeList(homeInfos, tastes, userSeq, pageable);
            return ResponseEntity.ok().body(homeInfos);
        }
    }


}
