package hotsixturtles.tupli.dto.simple;

import hotsixturtles.tupli.dto.PlayroomDto;
import hotsixturtles.tupli.entity.Playlist;
import hotsixturtles.tupli.entity.Playroom;
import hotsixturtles.tupli.entity.youtube.YoutubeVideo;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class SimplePlayroomCategoryDto {


    private Long id;
    private String title;
    private String content;
    private String tags;
    private Boolean isPublic;
    private String thumbnail;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private List<SimpleYoutubeVideoDto> videos;

    // 추가 변수
    private String nickname;
    private Integer likesCnt;
    private Boolean isLiked;

   public SimplePlayroomCategoryDto(Playroom playroom) {
        this.id = playroom.getId();
        this.title = playroom.getTitle();
        this.content = playroom.getContent();
        this.tags = playroom.getTags();
        this.isPublic = playroom.getIsPublic();
        this.videos = playroom.getVideos()
                .stream().map(x -> new SimpleYoutubeVideoDto(x)).collect(Collectors.toList());
        this.thumbnail = this.videos.get(0).getThumbnail(); // 맨앞 걸로 섬네일 고정.. 어케바꿀까용..
        this.startTime = playroom.getStartTime();
        this.endTime = playroom.getEndTime();

        // 추가 변수
       this.nickname = playroom.getUser().getNickname();
       this.likesCnt = playroom.getPlayroomLikes().size();
       this.isLiked = false;
   }

    public SimplePlayroomCategoryDto(Playroom playroom, Boolean isLiked) {
        this.id = playroom.getId();
        this.title = playroom.getTitle();
        this.content = playroom.getContent();
        this.tags = playroom.getTags();
        this.isPublic = playroom.getIsPublic();
        this.videos = playroom.getVideos()
                .stream().map(x -> new SimpleYoutubeVideoDto(x)).collect(Collectors.toList());
        this.thumbnail = this.videos.get(0).getThumbnail(); // 맨앞 걸로 섬네일 고정.. 어케바꿀까용..
        this.startTime = playroom.getStartTime();
        this.endTime = playroom.getEndTime();

        // 추가 변수
        this.nickname = playroom.getUser().getNickname();
        this.likesCnt = playroom.getPlayroomLikes().size();
        this.isLiked = isLiked;
    }
}
