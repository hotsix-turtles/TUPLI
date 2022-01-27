package hotsixturtles.tupli.entity.likes;

import hotsixturtles.tupli.entity.Board;
import hotsixturtles.tupli.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "board_likes")
@Getter @Setter
@NoArgsConstructor
public class BoardLikes {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="board_id")
    private Board board;

}