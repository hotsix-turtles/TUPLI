package hotsixturtles.tupli.dto.simple;

import hotsixturtles.tupli.entity.Playlist;
import hotsixturtles.tupli.entity.meta.PlaylistInfo;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;

public class SimplePlaylistDto {

    private Long id;
    private String name;
    private String description;
    private String image;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private PlaylistInfo playlistInfo;

    public SimplePlaylistDto (Playlist playlist) {
        this.id = playlist.getId();
        this.name = playlist.getName();
        this.description = playlist.getDescription();
        this.image = playlist.getImage();
        this.createdAt = playlist.getCreatedAt();
        this.updatedAt = playlist.getUpdatedAt();
        this.playlistInfo = playlist.getPlaylistInfo();
    }
}
