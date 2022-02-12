package hotsixturtles.tupli.dto;

import hotsixturtles.tupli.dto.simple.SimpleBadgeDto;
import hotsixturtles.tupli.dto.simple.SimpleUserDto;
import hotsixturtles.tupli.entity.Board;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class BoardDto {

    private Long id;
    private String title;
    private String content;
    private String type; // playroom, playlist, null
    private String typeId; // 대상 type Id

    private OffsetDateTime created;
    private OffsetDateTime updated;

    // 연결
    private SimpleUserDto user;
//    private List<Comment> comments;
//    private List<BoardLikes> boardLikes;

    // 제작 변수
    private Integer likes_count;

    private List<SimpleBadgeDto> badges;

    public BoardDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.created = board.getCreated();
        this.updated = board.getUpdated();

        // 연결
        this.user = new SimpleUserDto(board.getUser());

        // 변수
        this.likes_count = board.getBoardLikes() == null ? 0 : board.getBoardLikes().size();

    }


    public BoardDto(Board board, List<SimpleBadgeDto> simpleBadgeDtoList) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.created = board.getCreated();
        this.updated = board.getUpdated();

        // 연결
        this.user = new SimpleUserDto(board.getUser());

        // 변수
        this.likes_count = board.getBoardLikes() == null ? 0 : board.getBoardLikes().size();

        this.badges = simpleBadgeDtoList;
    }
}
