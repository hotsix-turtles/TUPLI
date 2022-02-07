package hotsixturtles.tupli.dto.response;

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
    private Map<Long, List<String>> playlists;  // 들어오기만 하고 나가지는 않음
    private List<Long> inviteIds;  // 들어오기만 하고 나가지는 않음
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;

    // 연결
    private SimpleUserDto user;
    private List<SimpleYoutubeVideoDto> videos;


    public ResponsePlayroomDto(Playroom playroom) {
        this.id = playroom.getId();
        this.title = playroom.getTitle();
        this.content = playroom.getContent();
        this.isPublic = playroom.getIsPublic();
        this.tags = playroom.getTags();
//        this.playlists = playlists;
//        this.inviteIds = inviteIds;
        this.startTime = playroom.getStartTime();
        this.endTime = playroom.getEndTime();

        // 연결
        this.user = new SimpleUserDto(playroom.getUser());
        this.videos = playroom.getVideos()
                .stream().map(x -> new SimpleYoutubeVideoDto(x)).collect(Collectors.toList());
    }

}
