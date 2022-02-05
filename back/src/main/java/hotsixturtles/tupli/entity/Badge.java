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
    @Column(name = "BADGE_SEQ")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long badgeSeq;

    @Column(name = "BADGE_NAME")
    private String badgeName;

}
