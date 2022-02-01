package hotsixturtles.tupli.dto.simple;

import hotsixturtles.tupli.entity.youtube.YoutubeVideo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SimpleYoutubeVideoDto {

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

    public SimpleYoutubeVideoDto(YoutubeVideo youtubeVideo) {
        this.id = youtubeVideo.getId();
        this.url = youtubeVideo.getUrl();
        this.thumbnails = youtubeVideo.getThumbnails();
        this.channelTitle = youtubeVideo.getChannelTitle();
        this.channelId = youtubeVideo.getChannelId();
        this.title = youtubeVideo.getTitle();
        this.titleLocalized = youtubeVideo.getTitleLocalized();
        this.categoryId = youtubeVideo.getCategoryId();
    }
}
