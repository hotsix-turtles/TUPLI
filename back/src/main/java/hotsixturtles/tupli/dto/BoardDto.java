package hotsixturtles.tupli.dto;

import hotsixturtles.tupli.dto.simple.SimpleUserDto;
import hotsixturtles.tupli.entity.Board;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class BoardDto {

    private Long id;
    private String title;
    private String content;
    private OffsetDateTime created;
    private OffsetDateTime updated;

    // 연결
    private SimpleUserDto user;
//    private List<Comment> comments;
//    private List<BoardLikes> boardLikes;

    // 제작 변수
    private Integer likes_count;


    public BoardDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.created = board.getCreated();
        this.updated = board.getUpdated();

        // 연결
        this.user = new SimpleUserDto(board.getUser());

    }
}
