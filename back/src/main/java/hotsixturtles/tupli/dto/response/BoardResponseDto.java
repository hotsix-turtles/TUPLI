package hotsixturtles.tupli.dto.response;

import hotsixturtles.tupli.dto.PlaylistDto;
import hotsixturtles.tupli.dto.PlayroomDto;
import hotsixturtles.tupli.dto.simple.SimpleBadgeDto;
import hotsixturtles.tupli.dto.simple.SimplePlaylistDto;
import hotsixturtles.tupli.dto.simple.SimpleUserDto;
import hotsixturtles.tupli.entity.Board;
import hotsixturtles.tupli.entity.Playlist;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class BoardResponseDto {

    private Long id;
    private String title;
    private String content;
    private String type; // null, "playlist", "playroom"
    private String image;

    private OffsetDateTime created;
    private OffsetDateTime updated;

    // 연결
    private SimpleUserDto user;
    private PlaylistDto playlist;  // $$$ 위험한 코드! 아직은 연결 없는데 아직은...
    private PlayroomDto playroom;  // $$$ 위험한 코드! 아직은 연결 없는데 아직은...
//    private List<Comment> comments;
//    private List<BoardLikes> boardLikes;

    // 제작 변수
    private Integer likes_count;

    private List<SimpleBadgeDto> badges;

    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.type = board.getType();
        this.created = board.getCreated();
        this.updated = board.getUpdated();

        // 연결
        this.user = new SimpleUserDto(board.getUser());
        if (board.getPlaylist() != null) {
            this.playlist = new PlaylistDto(board.getPlaylist());
            this.image = board.getPlaylist().getImage();
        }
        if (board.getPlayroom() != null) {
            this.playroom = new PlayroomDto(board.getPlayroom());
            this.image = board.getPlayroom().getImage();
        }

        // 변수
        this.likes_count = board.getBoardLikes() == null ? 0 : board.getBoardLikes().size();

    }


    public BoardResponseDto(Board board, List<SimpleBadgeDto> simpleBadgeDtoList) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.type = board.getType();
        this.created = board.getCreated();
        this.updated = board.getUpdated();

        // 연결
        this.user = new SimpleUserDto(board.getUser());
        if (board.getPlaylist() != null) {
            this.playlist = new PlaylistDto(board.getPlaylist());
            this.image = board.getPlaylist().getImage();
        }
        if (board.getPlayroom() != null) {
            this.playroom = new PlayroomDto(board.getPlayroom());
            this.image = board.getPlayroom().getImage();
        }

        // 변수
        this.likes_count = board.getBoardLikes() == null ? 0 : board.getBoardLikes().size();

        this.badges = simpleBadgeDtoList;
    }
}
