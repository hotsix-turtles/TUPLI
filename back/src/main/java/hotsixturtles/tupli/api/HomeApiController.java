package hotsixturtles.tupli.api;

import hotsixturtles.tupli.dto.BoardDto;
import hotsixturtles.tupli.dto.PlaylistDto;
import hotsixturtles.tupli.dto.PlayroomDto;
import hotsixturtles.tupli.entity.Board;
import hotsixturtles.tupli.entity.Playlist;
import hotsixturtles.tupli.entity.Playroom;
import hotsixturtles.tupli.service.BoardService;
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

    // 좋아요 이런 순이면 어짜피 고쳐야함...


    @GetMapping("/home/board")
    public ResponseEntity<?> getHomeBoardList(@PageableDefault(size = 10, sort ="id",  direction = Sort.Direction.DESC) Pageable pageable){
        List<Board> boardList= boardService.getHomeBoardList(pageable);
        if(boardList.size() == 0) return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        List<BoardDto> result = boardList.stream().map(b -> new BoardDto(b)).collect(Collectors.toList());

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



}
