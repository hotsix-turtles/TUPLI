package hotsixturtles.tupli.api;

import hotsixturtles.tupli.dto.response.BoardResponseDto;
import hotsixturtles.tupli.dto.PlaylistDto;
import hotsixturtles.tupli.dto.PlayroomDto;
import hotsixturtles.tupli.entity.Board;
import hotsixturtles.tupli.entity.Playlist;
import hotsixturtles.tupli.entity.Playroom;
import hotsixturtles.tupli.service.BoardService;
import hotsixturtles.tupli.service.HomeInfoService;
import hotsixturtles.tupli.service.PlaylistService;
import hotsixturtles.tupli.service.PlayroomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HomeApiController {

    private final BoardService boardService;
    private final PlaylistService playlistService;
    private final PlayroomService playroomService;
    private final HomeInfoService homeInfoService;

    // 이 부분은 하영님이 명세 작성해주신대여 그거 나오면 좀 달라질듯
    @GetMapping("/home/board")
    public ResponseEntity<?> getHomeBoardList(@PageableDefault(size = 10, sort ="id",  direction = Sort.Direction.DESC) Pageable pageable){
        List<Board> boardList= boardService.getHomeBoardList(pageable);
        if(boardList.size() == 0) return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        List<BoardResponseDto> result = boardList.stream().map(b -> new BoardResponseDto(b)).collect(Collectors.toList());

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/home/playlist")
    public ResponseEntity<?> getHomePlayLists(@PageableDefault(size = 10, sort ="id",  direction = Sort.Direction.DESC) Pageable pageable){
            List<Playlist> playlists = playlistService.getHomePlaylists(pageable);
            if(playlists.size() == 0) return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            List<PlaylistDto> result = playlists.stream().map(b -> new PlaylistDto(b)).collect(Collectors.toList());

            return ResponseEntity.ok().body(result);

    }

    @GetMapping("/home/playroom")
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
    public ResponseEntity<?> getAllList(@PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        List<Object> homeInfos = homeInfoService.getHomeInfoList(pageable);
        if(homeInfos.size() == 0) return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);

        return ResponseEntity.ok().body(homeInfos);
    }


}
