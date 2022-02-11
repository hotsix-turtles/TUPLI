package hotsixturtles.tupli.dto.simple;

import hotsixturtles.tupli.entity.Comment;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class SimpleCommentDto {

    private Long id;
    private String content;
    private String emoticon;
    private OffsetDateTime created;
    private OffsetDateTime updated;

    public SimpleCommentDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.created = comment.getCreated();
        this.updated = comment.getUpdated();
        this.emoticon = comment.getEmoticon();
    }

}
