package hotsixturtles.tupli.entity;

import hotsixturtles.tupli.entity.likes.BoardLikes;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BOARD")
@Getter @Setter
@NoArgsConstructor//(access = AccessLevel.PROTECTED)
public class Board {

    @Id @GeneratedValue
    private Long id;

    @Size(max = 200)
    private String title;
    private String content;
    private String type; // null, "playlist", "playroom"

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
    @JoinColumn(name="user_seq")
    private User user;

    @OneToMany(mappedBy = "board", cascade = {CascadeType.REMOVE})
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "board")
    private List<BoardLikes> boardLikes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="playlist_id")
    private Playlist playlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="playroom_id")
    private Playroom playroom;


}
