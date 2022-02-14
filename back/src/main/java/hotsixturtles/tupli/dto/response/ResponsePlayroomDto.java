package hotsixturtles.tupli.dto.response;

import hotsixturtles.tupli.dto.PlaylistDto;
import hotsixturtles.tupli.dto.simple.SimpleUserDto;
import hotsixturtles.tupli.dto.simple.SimpleYoutubeVideoDto;
import hotsixturtles.tupli.entity.Playroom;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ResponsePlayroomDto {
    private Long id;

    private String title;
    private String content;
    private Boolean isPublic;
    private String tags;
    private Map<Long, List<Long>> playlists;
    private List<Long> inviteIds;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;

    // 연결
    private SimpleUserDto user;
    private List<SimpleYoutubeVideoDto> videos;
    private List<PlaylistDto> playlistsInfo;

    private Integer userCount;
    private Integer likesCnt;
    private Integer userCountMax;

    public ResponsePlayroomDto(Playroom playroom) {
        this.id = playroom.getId();
        this.title = playroom.getTitle();
        this.content = playroom.getContent();
        this.isPublic = playroom.getIsPublic();
        this.tags = playroom.getTags();
        this.playlists = playroom.getPlaylists();
        this.inviteIds = inviteIds;
        this.startTime = playroom.getStartTime();
        this.endTime = playroom.getEndTime();
        this.userCount = playroom.getUserCount();
        this.likesCnt = playroom.getLikesCnt();
        this.userCountMax = playroom.getUserCountMax();

        // 연결
        this.user = new SimpleUserDto(playroom.getUser());
        this.videos = playroom.getVideos()
                .stream().map(x -> new SimpleYoutubeVideoDto(x)).collect(Collectors.toList());

    }

}
