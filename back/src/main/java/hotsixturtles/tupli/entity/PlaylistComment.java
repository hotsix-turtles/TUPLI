package hotsixturtles.tupli.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "playlist_comment")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaylistComment {

    @Id
    @GeneratedValue
    private Long id;

    private String content;
    private String emoticon = null;  // 이게 null 값이면 content 표기, 아니면 이모티콘 표기

    @Column(name = "created_at")
    private OffsetDateTime created;

    @PrePersist
    private void beforeSaving() {
        created = OffsetDateTime.now();
        updated = OffsetDateTime.now();
    }

    @Column(name = "updated_at")
    private OffsetDateTime updated;

    @PreUpdate
    private void beforeUpdating() {
        updated = OffsetDateTime.now();
    }


    // 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="playlist_id")
    private Playlist playlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;
}
