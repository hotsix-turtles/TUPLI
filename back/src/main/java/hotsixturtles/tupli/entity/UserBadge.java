package hotsixturtles.tupli.entity;

import lombok.*;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "user_badge")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserBadge {

    @Id
    @Column(name = "USER_BADGE_SEQ")
    @GeneratedValue
    private Long userBadgeSeq;

    private Long userSeq;

    private Long badgeSeq;

    @Column(name = "ACQUIRED_AT")
    private OffsetDateTime acquired;

    @PrePersist
    private void beforeSaving() {
        acquired = OffsetDateTime.now();
    }

    @PreUpdate
    private void beforeUpdating() {
        acquired = OffsetDateTime.now();
    }

    public UserBadge(Long userSeq, Long badgeSeq){
        this.userSeq = userSeq;
        this.badgeSeq = badgeSeq;
    }


}
