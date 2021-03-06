package hotsixturtles.tupli.dto;

import hotsixturtles.tupli.dto.simple.SimpleBadgeDto;
import hotsixturtles.tupli.dto.simple.SimpleUserDto;
import hotsixturtles.tupli.dto.simple.SimpleYoutubeVideoDto;
import hotsixturtles.tupli.entity.Badge;
import hotsixturtles.tupli.entity.Playroom;
import hotsixturtles.tupli.entity.User;
import hotsixturtles.tupli.entity.likes.PlayroomLikes;
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
    private List<Long> guests;

    private Integer userCount;
    private Integer likesCnt;
    private Integer userCountMax;

    // 연결
    private SimpleUserDto user;
    private List<SimpleYoutubeVideoDto> videos;
    private List<PlaylistDto> playlistsInfo;

    // 특수변수
//    private List<SimpleUserDto> guest;  // 참여자 정보 다 보내는 버전
    private List<SimpleBadgeDto> badges;

    private Boolean isLiked;


    public PlayroomDto(Playroom playroom) {
        this.id = playroom.getId();
        this.title = playroom.getTitle();
        this.content = playroom.getContent();
        this.isPublic = playroom.getIsPublic();
        this.tags = playroom.getTags();
        this.playlists = playroom.getPlaylists();
        this.inviteIds = playroom.getInviteIds();
        this.startTime = playroom.getStartTime();
        this.endTime = playroom.getEndTime();
        this.guests = playroom.getGuests();
        this.userCount = playroom.getUserCount();
        this.likesCnt = playroom.getLikesCnt();
        this.userCountMax = playroom.getUserCountMax();
        // 연결
        this.user = new SimpleUserDto(playroom.getUser());
        this.videos = playroom.getVideos()
                .stream().map(x -> new SimpleYoutubeVideoDto(x)).collect(Collectors.toList());
    }

    public PlayroomDto(Playroom playroom, User user) {
        this.id = playroom.getId();
        this.title = playroom.getTitle();
        this.content = playroom.getContent();
        this.isPublic = playroom.getIsPublic();
        this.tags = playroom.getTags();
        this.playlists = playroom.getPlaylists();
        this.inviteIds = playroom.getInviteIds();
        this.startTime = playroom.getStartTime();
        this.endTime = playroom.getEndTime();
        this.guests = playroom.getGuests();
        this.userCount = playroom.getUserCount();
        this.likesCnt = playroom.getLikesCnt();
        this.userCountMax = playroom.getUserCountMax();
        // 연결
        this.user = new SimpleUserDto(playroom.getUser());
        this.videos = playroom.getVideos()
                .stream().map(x -> new SimpleYoutubeVideoDto(x)).collect(Collectors.toList());

        if(playroom.getPlayroomLikes() == null){
            this.isLiked = false;
        }
        else{
            this.isLiked = false;
            for(PlayroomLikes nowPlayroomLikes : playroom.getPlayroomLikes()){
                if(nowPlayroomLikes.getUser().getUserSeq() == user.getUserSeq()){
                    this.isLiked = true;
                    break;
                }
            }
        }
    }

//    // 참여자 정보까지 다 받아가는 버전
//    public PlayroomDto(Playroom playroom, List<User> guests) {
//        this.id = playroom.getId();
//        this.title = playroom.getTitle();
//        this.content = playroom.getContent();
//        this.isPublic = playroom.getIsPublic();
//        this.tags = playroom.getTags();
//        this.playlists = playroom.getPlaylists();
//        this.inviteIds = inviteIds;
//        this.startTime = playroom.getStartTime();
//        this.endTime = playroom.getEndTime();
//        this.userCount = playroom.getUserCount();
//        this.likesCnt = playroom.getLikesCnt();
//        this.userCountMax = playroom.getUserCountMax();
//
//        // 연결
//        this.user = new SimpleUserDto(playroom.getUser());
//        this.videos = playroom.getVideos()
//                .stream().map(x -> new SimpleYoutubeVideoDto(x)).collect(Collectors.toList());
//
//        // 참여자 확인
//        this.guest = guests.stream().map(x -> new SimpleUserDto(x)).collect(Collectors.toList());
//
//    }

}
