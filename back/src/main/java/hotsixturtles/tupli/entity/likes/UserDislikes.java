package hotsixturtles.tupli.entity.likes;

import hotsixturtles.tupli.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user_dislikes")
@Getter @Setter
@NoArgsConstructor
public class UserDislikes {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id_dislike")
    private User fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="to_user_id_dislike")
    private User toUser;
}
