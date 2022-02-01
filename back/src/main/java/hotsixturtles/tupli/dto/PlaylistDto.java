package hotsixturtles.tupli.dto;

import hotsixturtles.tupli.dto.simple.SimpleUserDto;
import hotsixturtles.tupli.entity.Playlist;
import hotsixturtles.tupli.entity.likes.PlaylistLikes;
import hotsixturtles.tupli.entity.meta.PlaylistInfo;
import lombok.Data;


import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PlaylistDto {

    private Long id;
    private String name;
    private String description;
    private String image;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private PlaylistInfo playlistInfo;

    // 연결
    private SimpleUserDto user;
//    private List<YoutubeVideo> youtubeVideos;
    private List<SimpleUserDto> playlistLikes;

    public PlaylistDto(Playlist playlist) {
        this.id = playlist.getId();
        this.name = playlist.getName();
        this.description = playlist.getDescription();
        this.image = playlist.getImage();
        this.createdAt = playlist.getCreatedAt();
        this.updatedAt = playlist.getUpdatedAt();
        this.playlistInfo = playlist.getPlaylistInfo();

        // 연결
        this.user = new SimpleUserDto(playlist.getUser());
        this.playlistLikes = playlist.getPlaylistLikes()
                .stream().map(x-> new SimpleUserDto(x.getUser())).collect(Collectors.toList());
    }
}
