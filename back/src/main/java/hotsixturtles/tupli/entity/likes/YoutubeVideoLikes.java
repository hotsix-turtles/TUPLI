package hotsixturtles.tupli.entity.likes;

import hotsixturtles.tupli.entity.Playlist;
import hotsixturtles.tupli.entity.User;
import hotsixturtles.tupli.entity.youtube.YoutubeVideo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * 좋아요 한 영상
 */
@Entity
@Table(name = "youtube_video_likes")
@Getter @Setter
@NoArgsConstructor
public class YoutubeVideoLikes {

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
