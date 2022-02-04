package hotsixturtles.tupli.dto.simple;

import hotsixturtles.tupli.entity.Playlist;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Data
public class SimplePlaylistDto {


    private Long id;
    private String title;
    private String content;
    private String tags;
    private Boolean isPublic;
    private String image;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private ConcurrentHashMap<Integer, Integer> playlistInfo;

    public SimplePlaylistDto (Playlist playlist) {
        this.id = playlist.getId();
        this.title = playlist.getTitle();
        this.content = playlist.getContent();
        this.tags = playlist.getTags();
        this.isPublic = playlist.getIsPublic();
        this.image = playlist.getImage();
        this.createdAt = playlist.getCreatedAt();
        this.updatedAt = playlist.getUpdatedAt();
        this.playlistInfo = playlist.getPlaylistInfo();
    }
}
