package hotsixturtles.tupli.dto;

import hotsixturtles.tupli.dto.simple.SimpleBadgeDto;
import hotsixturtles.tupli.dto.simple.SimpleUserDto;
import hotsixturtles.tupli.dto.simple.SimpleYoutubeVideoDto;
import hotsixturtles.tupli.entity.Badge;
import hotsixturtles.tupli.entity.Playroom;
import hotsixturtles.tupli.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class PlayroomDto {

    // get(playroom/{playroomid} 에서 사용중입니다 response용
    private Long id;

    private String title;
    private String content;
    private Boolean isPublic;
    private String tags;
    private Map<Long, List<Long>> playlists;
    private List<Long> inviteIds;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;

    private Integer userCount;
    private Integer likesCnt;

    // 연결
    private SimpleUserDto user;
    private List<SimpleYoutubeVideoDto> videos;

    // 특수변수
    private List<SimpleUserDto> guest;
    private List<SimpleBadgeDto> badges;


    public PlayroomDto(Playroom playroom) {
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

        // 연결
        this.user = new SimpleUserDto(playroom.getUser());
        this.videos = playroom.getVideos()
                .stream().map(x -> new SimpleYoutubeVideoDto(x)).collect(Collectors.toList());
    }


    public PlayroomDto(Playroom playroom, List<User> guests) {
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

        // 연결
        this.user = new SimpleUserDto(playroom.getUser());
        this.videos = playroom.getVideos()
                .stream().map(x -> new SimpleYoutubeVideoDto(x)).collect(Collectors.toList());

        // 참여자 확인
        this.guest = guests.stream().map(x -> new SimpleUserDto(x)).collect(Collectors.toList());

    }

}
