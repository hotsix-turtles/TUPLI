package hotsixturtles.tupli.dto.simple.home;

import hotsixturtles.tupli.dto.simple.SimpleUserDto;
import hotsixturtles.tupli.dto.simple.SimpleYoutubeVideoDto;
import hotsixturtles.tupli.entity.Playroom;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SimpleHomePlayroomDto {

    private String type = "playroom";
    private Long id;

    private String title;
    private Boolean isPublic;
    private String tags;
    private Map<Long, List<Long>> playlists;

    private Integer userCount;
    private Integer likesCnt;

    private SimpleYoutubeVideoDto videos;

    // 유저 정보

    private Long userId;
    private String nickName;
    private String userProfileImg;
    private Integer userFollowersCnt;

    // 특수변수
//    private List<SimpleUserDto> guest;  // 참여자 정보 다 보내는 버전


    public SimpleHomePlayroomDto(Playroom playroom) {
        this.id = playroom.getId();
        this.title = playroom.getTitle();
        this.isPublic = playroom.getIsPublic();
        this.tags = playroom.getTags();
        this.playlists = playroom.getPlaylists();
        this.userCount = playroom.getUserCount();
        this.likesCnt = playroom.getLikesCnt();
        this.userId = playroom.getUser().getUserSeq();
        this.nickName = playroom.getUser().getNickname();
        this.userProfileImg = playroom.getUser().getProfileImage();
        this.userFollowersCnt = playroom.getUser().getTo_user() == null ? 0 : playroom.getUser().getTo_user().size();

        if(playroom.getVideos() == null || playroom.getVideos().size() == 0) this.videos = null;
        else this.videos = new SimpleYoutubeVideoDto(playroom.getVideos().get(0));
    }


}
