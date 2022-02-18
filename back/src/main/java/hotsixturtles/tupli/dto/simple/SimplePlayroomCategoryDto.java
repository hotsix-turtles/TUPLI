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
    private String image;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private List<SimpleYoutubeVideoDto> videos;
    private Integer userCount;

    // 추가 변수
    private Long userId;
    private String nickname;
    private String profileImg;
    private Integer likesCnt;
    private Boolean isLiked;

    private Integer userCountMax;

   public SimplePlayroomCategoryDto(Playroom playroom) {
        this.id = playroom.getId();
        this.title = playroom.getTitle();
        this.content = playroom.getContent();
        this.tags = playroom.getTags();
        this.isPublic = playroom.getIsPublic();
        this.videos = playroom.getVideos()
                .stream().map(x -> new SimpleYoutubeVideoDto(x)).collect(Collectors.toList());
        this.image = playroom.getImage();
        this.startTime = playroom.getStartTime();
        this.endTime = playroom.getEndTime();
        if(playroom.getGuests() == null || playroom.getGuests().size() == 0){
            this.userCount = 0;
        }
        else this.userCount = playroom.getGuests().size();
        this.userCountMax = playroom.getUserCountMax();

        // 추가 변수
       this.userId = playroom.getUser().getUserSeq();
       this.nickname = playroom.getUser().getNickname();
       this.profileImg = playroom.getUser().getProfileImage();

       this.likesCnt = playroom.getPlayroomLikes() == null ? 0 : playroom.getPlayroomLikes().size();
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
        this.image = playroom.getImage();
        this.startTime = playroom.getStartTime();
        this.endTime = playroom.getEndTime();
        if(playroom.getGuests() == null || playroom.getGuests().size() == 0){
            this.userCount = 0;
        }
        else this.userCount = playroom.getGuests().size();
        this.userCountMax = playroom.getUserCountMax();

        // 추가 변수
        this.userId = playroom.getUser().getUserSeq();
        this.nickname = playroom.getUser().getNickname();
        this.profileImg = playroom.getUser().getProfileImage();
        this.likesCnt = playroom.getPlayroomLikes() == null ? 0 : playroom.getPlayroomLikes().size();
        this.isLiked = isLiked;
    }
}
