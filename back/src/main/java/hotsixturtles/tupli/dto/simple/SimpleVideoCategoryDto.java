package hotsixturtles.tupli.dto.simple;

import hotsixturtles.tupli.entity.youtube.YoutubeVideo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SimpleVideoCategoryDto {
    private Long id;

    private String videoId;  // url로 쓰임. 예시 : oODreucZqmM, https://www.youtube.com/watch?v= 주소와 조합하면 영상이 됨
    private String title;  // 영상 제목
    private String date;  // ? <== 업로드 날짜 아닐까여?..
    private String thumbnail;  // 썸네일 주소, 풀 주소가 들어온다.
    private String channelTitle;  // 영상 올린 채널
    private String duration;  // 영상길이
    private Integer categoryId;  // 유튜브 기준 카테고리 분류

    // 추가 변수
    private Boolean isLiked;

    public SimpleVideoCategoryDto(YoutubeVideo youtubeVideo) {
        this.id = youtubeVideo.getId();
        this.videoId = youtubeVideo.getVideoId();
        this.title = youtubeVideo.getTitle();
        this.date = youtubeVideo.getDate();
        this.thumbnail = youtubeVideo.getThumbnail();
        this.channelTitle = youtubeVideo.getChannelTitle();
        this.duration = youtubeVideo.getDuration();
        this.categoryId = youtubeVideo.getCategoryId();

        this.isLiked = false;
    }
    public SimpleVideoCategoryDto(YoutubeVideo youtubeVideo, Boolean isLiked) {
        this.id = youtubeVideo.getId();
        this.videoId = youtubeVideo.getVideoId();
        this.title = youtubeVideo.getTitle();
        this.date = youtubeVideo.getDate();
        this.thumbnail = youtubeVideo.getThumbnail();
        this.channelTitle = youtubeVideo.getChannelTitle();
        this.duration = youtubeVideo.getDuration();
        this.categoryId = youtubeVideo.getCategoryId();

        this.isLiked = isLiked;
    }
}
