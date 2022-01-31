package hotsixturtles.tupli.entity.youtube;

import hotsixturtles.tupli.dto.simple.SimpleYoutubeVideoDto;
import hotsixturtles.tupli.entity.Playlist;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "youtube_video")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class YoutubeVideo {

    @Id
    @GeneratedValue
    private Long id;

    // 영상 참조 정보
    private String url;  // 예시 : oODreucZqmM, https://www.youtube.com/watch?v= 주소와 조합하면 영상이 됨
    private String thumbnails;  // 썸네일 주소 중 하나, 풀 주소가 들어온다.

    // 영상 자체 정보
    private String channelTitle;  // 영상 올린 채널
    private String channelId;  // 영상 올린 채널 ID 예시 : UCbp9MyKCTEww4CxEzc_Tp0Q, https://www.youtube.com/channel/ 주소와 조합하면 채널로 이동
    private String title;  // 영상 제목
    private String titleLocalized;  // 영상 현지화 제목
    private String categoryId;  // 유튜브 기준 카테고리 분류

    // 연결(???)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="playlist_id")
    private Playlist playlist;

    // Setter (좋아요, 저장하기 둘 다 대응 가능하고, 각각의 로직 추적 및 수정이 쉬워짐)
    public void setInit(SimpleYoutubeVideoDto simpleYoutubeVideoDto) {
        this.url =simpleYoutubeVideoDto.getUrl();
        this.thumbnails = simpleYoutubeVideoDto.getThumbnails();
        this.channelTitle = simpleYoutubeVideoDto.getChannelTitle();
        this.channelId = simpleYoutubeVideoDto.getChannelId();
        this.title = simpleYoutubeVideoDto.getTitle();
        this.titleLocalized = simpleYoutubeVideoDto.getTitleLocalized();
        this.categoryId = simpleYoutubeVideoDto.getCategoryId();
    }
    

}
