package hotsixturtles.tupli.dto;

import hotsixturtles.tupli.dto.simple.SimpleUserDto;
import hotsixturtles.tupli.entity.Playlist;
import lombok.Data;


import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Data
public class PlaylistDto {

    private Long id;
    private String title;
    private String content;
    private String tags;
    private Boolean isPublic;
    private String image;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    // 별도 정보 ??
    private ConcurrentHashMap<Integer, Integer> playlistInfo;

    // 연결
    private SimpleUserDto user;
//    private List<YoutubeVideo> youtubeVideos;
    private List<SimpleUserDto> playlistLikes;

    public PlaylistDto(Playlist playlist) {
        this.id = playlist.getId();
        this.title = playlist.getTitle();
        this.content = playlist.getContent();
        this.tags = playlist.getTags();
        this.isPublic = playlist.getIsPublic();
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
