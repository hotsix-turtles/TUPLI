package hotsixturtles.tupli.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "badge")
@Builder
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Badge {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "BADGE_SEQ")
    private Long badgeSeq;

    @Column(name = "BADGE_NAME")
    private String badgeName;

}
