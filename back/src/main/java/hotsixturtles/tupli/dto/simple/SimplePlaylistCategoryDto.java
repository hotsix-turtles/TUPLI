package hotsixturtles.tupli.dto.simple;

import hotsixturtles.tupli.entity.Playlist;
import hotsixturtles.tupli.entity.likes.PlaylistLikes;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class SimplePlaylistCategoryDto {


    private Long id;
    private String title;
    private String content;
    private String tags;
    private Boolean isPublic;
    private String image;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    // 추가 변수수
    private String nickname;
    private Integer likesCnt;
    private Boolean isLiked;

   public SimplePlaylistCategoryDto(Playlist playlist) {
        this.id = playlist.getId();
        this.title = playlist.getTitle();
        this.content = playlist.getContent();
        this.tags = playlist.getTags();
        this.isPublic = playlist.getIsPublic();
        this.image = playlist.getImage();
        this.createdAt = playlist.getCreatedAt();
        this.updatedAt = playlist.getUpdatedAt();

        // 추가 변수
       this.nickname = playlist.getUser().getNickname();
       this.likesCnt = playlist.getPlaylistLikes() == null ? 0 : playlist.getPlaylistLikes().size();
       this.isLiked = false;
   }

    public SimplePlaylistCategoryDto(Playlist playlist, Boolean isLiked) {
        this.id = playlist.getId();
        this.title = playlist.getTitle();
        this.content = playlist.getContent();
        this.tags = playlist.getTags();
        this.isPublic = playlist.getIsPublic();
        this.image = playlist.getImage();
        this.createdAt = playlist.getCreatedAt();
        this.updatedAt = playlist.getUpdatedAt();

        // 추가 변수
        this.nickname = playlist.getUser().getNickname();
        this.likesCnt = playlist.getPlaylistLikes() == null ? 0 : playlist.getPlaylistLikes().size();
        this.isLiked = isLiked;
    }
}
