package hotsixturtles.tupli.dto;

import hotsixturtles.tupli.dto.simple.SimpleUserDto;
import hotsixturtles.tupli.entity.Comment;
import hotsixturtles.tupli.entity.PlaylistComment;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class PlaylistCommentDto {

    private Long id;
    private String content;
    private String emoticon;
    private OffsetDateTime created;
    private OffsetDateTime updated;

    // 연결
    private SimpleUserDto user;

    public PlaylistCommentDto(PlaylistComment playlistComment) {
        this.id = playlistComment.getId();
        this.content = playlistComment.getContent();
        this.created = playlistComment.getCreated();
        this.updated = playlistComment.getUpdated();
        this.emoticon = playlistComment.getEmoticon();

        // 연결
        this.user = new SimpleUserDto(playlistComment.getUser());
    }
}
