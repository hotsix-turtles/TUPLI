package hotsixturtles.tupli.dto.simple.home;

import hotsixturtles.tupli.dto.simple.SimpleUserDto;
import hotsixturtles.tupli.dto.simple.SimpleYoutubeVideoDto;
import hotsixturtles.tupli.entity.Playlist;
import hotsixturtles.tupli.entity.User;
import hotsixturtles.tupli.entity.likes.PlaylistLikes;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class SimpleHomePlaylistDto {

    private String type = "playlist";
    private Long id;
    private String title;
    private String tags;
    private Boolean isPublic;
    private OffsetDateTime createdAt;

    // 유저 정보
    private Long userId;
    private String nickName;
    private String userProfileImg;
    private Integer userFollowersCnt;

    // 추가 정보
    private String userLikesYN;

    // 연결
    private Integer likesCnt;
    private List<SimpleUserDto> playlistLikes;
    private SimpleYoutubeVideoDto videos;


    public SimpleHomePlaylistDto(Playlist playlist) {
        this.id = playlist.getId();
        this.title = playlist.getTitle();
        this.tags = playlist.getTags();
        this.isPublic = playlist.getIsPublic();
        this.createdAt = playlist.getCreatedAt();
        //this.image = playlist.getImage();

        // 유저 정보
        this.userId = playlist.getUser().getUserSeq();
        this.nickName = playlist.getUser().getNickname();
        this.userProfileImg = playlist.getUser().getProfileImage();

        this.userFollowersCnt = playlist.getUser().getTo_user() == null ? 0 : playlist.getUser().getTo_user().size();

        // 연결
        this.likesCnt = playlist.getPlaylistLikes() == null ? 0 :playlist.getPlaylistLikes().size();
        this.playlistLikes = playlist.getPlaylistLikes()
                .stream().map(x-> new SimpleUserDto(x.getUser())).collect(Collectors.toList());

        if(playlist.getYoutubeVideos() == null || playlist.getYoutubeVideos().size() == 0){
            this.videos = null;
        }
        else this.videos = new SimpleYoutubeVideoDto(playlist.getYoutubeVideos().get(0));

    }
    public SimpleHomePlaylistDto(Playlist playlist, User user) {
        this.id = playlist.getId();
        this.title = playlist.getTitle();
        this.tags = playlist.getTags();
        this.isPublic = playlist.getIsPublic();
        this.createdAt = playlist.getCreatedAt();
        //this.image = playlist.getImage();

        // 유저 정보
        this.userId = playlist.getUser().getUserSeq();
        this.nickName = playlist.getUser().getNickname();
        this.userProfileImg = playlist.getUser().getProfileImage();

        this.userFollowersCnt = playlist.getUser().getTo_user() == null ? 0 : playlist.getUser().getTo_user().size();

        // 연결
        this.likesCnt = playlist.getPlaylistLikes() == null ? 0 :playlist.getPlaylistLikes().size();
        this.playlistLikes = playlist.getPlaylistLikes()
                .stream().map(x-> new SimpleUserDto(x.getUser())).collect(Collectors.toList());

        if(playlist.getYoutubeVideos() == null || playlist.getYoutubeVideos().size() == 0){
            this.videos = null;
        }
        else this.videos = new SimpleYoutubeVideoDto(playlist.getYoutubeVideos().get(0));

        if(playlist.getPlaylistLikes() == null){
            this.userLikesYN = "N";
        }
        else{
            this.userLikesYN = "N";
            for(PlaylistLikes nowPlaylistLikes : playlist.getPlaylistLikes()){
                if(nowPlaylistLikes.getUser().getUserSeq() == user.getUserSeq()){
                    this.userLikesYN = "Y";
                    break;
                }
            }
        }

    }
}
