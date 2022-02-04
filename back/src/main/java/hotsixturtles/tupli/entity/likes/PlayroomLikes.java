package hotsixturtles.tupli.entity.likes;

import hotsixturtles.tupli.entity.Playroom;
import hotsixturtles.tupli.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "playroom_likes")
@Getter
@Setter
@NoArgsConstructor
public class PlayroomLikes {

        @Id
        @GeneratedValue
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name="user_id")
        private User user;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name="playroom_id")
        private Playroom playroom;

}
