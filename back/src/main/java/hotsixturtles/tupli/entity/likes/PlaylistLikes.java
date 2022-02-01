package hotsixturtles.tupli.entity.likes;

import hotsixturtles.tupli.entity.Playlist;
import hotsixturtles.tupli.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "playlist_likes")
@Getter @Setter
@NoArgsConstructor
public class PlaylistLikes {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="playlist_id")
    private Playlist playlist;
}

