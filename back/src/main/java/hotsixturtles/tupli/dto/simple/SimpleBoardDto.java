package hotsixturtles.tupli.dto.simple;

import hotsixturtles.tupli.entity.Board;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class SimpleBoardDto {

    private Long id;
    private String title;
    private String content;
    private OffsetDateTime created;
    private OffsetDateTime updated;

    public SimpleBoardDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.created = board.getCreated();
        this.updated = board.getUpdated();
    }
}
