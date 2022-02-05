package hotsixturtles.tupli.entity.likes;

import hotsixturtles.tupli.entity.User;
import hotsixturtles.tupli.entity.youtube.YoutubeVideo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * 저장한 영상
 */
@Entity
@Table(name = "youtube_video_saves")
@Getter @Setter
@NoArgsConstructor
public class YoutubeVideoSaves {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="video_id")
    private YoutubeVideo youtubeVideo;

}
