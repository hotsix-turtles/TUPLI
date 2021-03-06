package hotsixturtles.tupli.entity.youtube;

import hotsixturtles.tupli.dto.simple.SimpleYoutubeVideoDto;
import hotsixturtles.tupli.entity.Board;
import hotsixturtles.tupli.entity.Playlist;
import hotsixturtles.tupli.entity.Playroom;
import hotsixturtles.tupli.entity.likes.PlaylistLikes;
import hotsixturtles.tupli.entity.likes.YoutubeVideoLikes;
import hotsixturtles.tupli.entity.likes.YoutubeVideoSaves;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "youtube_video")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class YoutubeVideo {

    @Id
    @GeneratedValue
    private Long id;

    private String videoId;  // url로 쓰임. 예시 : oODreucZqmM, https://www.youtube.com/watch?v= 주소와 조합하면 영상이 됨
    private String title;  // 영상 제목
    private String date;  // ? 업로드 날짜인듯 싶어용..
    private String thumbnail;  // 썸네일 주소, 풀 주소가 들어온다.
    private String channelTitle;  // 영상 올린 채널
    private String duration;  // 영상길이
    private Integer categoryId;  // 유튜브 기준 카테고리 분류

    // 플레이리스트 생성시에는 연결. (단순한 저장, 좋아요시에는 연결 없음)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="playlist_id")
    private Playlist playlist;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="playroom_id")
    private Playroom playroom;

    @OneToMany(mappedBy = "youtubeVideo", cascade = CascadeType.ALL)
    private List<YoutubeVideoLikes> youtubeLikes = new ArrayList<>();

    @OneToMany(mappedBy = "youtubeVideo", cascade = CascadeType.ALL)
    private List<YoutubeVideoSaves> youtubeSaves = new ArrayList<>();


    public void newVideo(SimpleYoutubeVideoDto simpleYoutubeVideoDto) {
        this.videoId = simpleYoutubeVideoDto.getVideoId();
        this.title = simpleYoutubeVideoDto.getTitle();
        this.date = simpleYoutubeVideoDto.getDate();
        this.thumbnail = simpleYoutubeVideoDto.getThumbnail();
        this.channelTitle = simpleYoutubeVideoDto.getChannelTitle();
        this.duration = simpleYoutubeVideoDto.getDuration();
        this.categoryId = simpleYoutubeVideoDto.getCategoryId();
    }

    public void copyVideo(YoutubeVideo youtubeVideo) {
        this.videoId = youtubeVideo.getVideoId();
        this.title = youtubeVideo.getTitle();
        this.date = youtubeVideo.getDate();
        this.thumbnail = youtubeVideo.getThumbnail();
        this.channelTitle = youtubeVideo.getChannelTitle();
        this.duration = youtubeVideo.getDuration();
        this.categoryId = youtubeVideo.getCategoryId();
    }
}
