package hotsixturtles.tupli.dto;

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
public class PlaylistDto {

    private Long id;
    private String title;
    private String content;
    private String tags;
    private Boolean isPublic;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    //private String image;  //  플레이리스트 자체 이미지 사용 예정 없음!

    // 유저 정보
    private Long userId;
    private String nickName;
    private String userProfileImg;
    private Integer userFollowersCnt;


    // 연결
    private Integer likesCnt;
    private List<SimpleUserDto> playlistLikes;
    private List<SimpleYoutubeVideoDto> videos;

    private Boolean isLiked;


    public PlaylistDto(Playlist playlist) {
        this.id = playlist.getId();
        this.title = playlist.getTitle();
        this.content = playlist.getContent();
        this.tags = playlist.getTags();
        this.isPublic = playlist.getIsPublic();
        this.createdAt = playlist.getCreatedAt();
        this.updatedAt = playlist.getUpdatedAt();
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
        this.videos = playlist.getYoutubeVideos()
                .stream().map(x -> new SimpleYoutubeVideoDto(x)).collect(Collectors.toList());
    }
    public PlaylistDto(Playlist playlist, User user) {
        this.id = playlist.getId();
        this.title = playlist.getTitle();
        this.content = playlist.getContent();
        this.tags = playlist.getTags();
        this.isPublic = playlist.getIsPublic();
        this.createdAt = playlist.getCreatedAt();
        this.updatedAt = playlist.getUpdatedAt();
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
        this.videos = playlist.getYoutubeVideos()
                .stream().map(x -> new SimpleYoutubeVideoDto(x)).collect(Collectors.toList());
        if(playlist.getPlaylistLikes() == null){
            this.isLiked = false;
        }
            else{
            this.isLiked = false;
            for(PlaylistLikes nowPlaylistLikes : playlist.getPlaylistLikes()){
                if(nowPlaylistLikes.getUser().getUserSeq() == user.getUserSeq()){
                    this.isLiked = true;
                    break;
                }
            }
        }
    }


}
