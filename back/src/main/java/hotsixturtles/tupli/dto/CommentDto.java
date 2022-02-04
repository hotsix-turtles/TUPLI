package hotsixturtles.tupli.dto;

import hotsixturtles.tupli.dto.simple.SimpleUserDto;
import hotsixturtles.tupli.entity.Comment;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class CommentDto {

    private Long id;
    private String content;
    private String emoticon;
    private OffsetDateTime created;
    private OffsetDateTime updated;

    // 연결
    private SimpleUserDto user;

    public CommentDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.created = comment.getCreated();
        this.updated = comment.getUpdated();
        this.emoticon = comment.getEmoticon();

        // 연결
        this.user = new SimpleUserDto(comment.getUser());
    }
}
