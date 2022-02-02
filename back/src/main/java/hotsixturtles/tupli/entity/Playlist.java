package hotsixturtles.tupli.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import hotsixturtles.tupli.entity.likes.PlaylistLikes;
import hotsixturtles.tupli.entity.meta.PlaylistInfo;
import hotsixturtles.tupli.entity.youtube.YoutubeVideo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "playlist")
@Getter
@Setter
@NoArgsConstructor//(access = AccessLevel.PROTECTED)
@TypeDef(name = "json", typeClass = JsonType.class)
public class Playlist {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String description;

    private String image;  // 플레이리스트 자체에 이미지 넣을지, 영상 썸네일 중 하나일지?

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    @PrePersist
    private void beforeSaving() {
        createdAt = OffsetDateTime.now();
        updatedAt = OffsetDateTime.now();
    }

    @PreUpdate
    private void beforeUpdating() {
        updatedAt = OffsetDateTime.now();
    }

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private PlaylistInfo playlistInfo;  // 유튜브 Tag + Custom Tag 리스트 등의 메타정보 조합

    // 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "playlist")
    private List<PlaylistLikes> playlistLikes = new ArrayList<>();

    @OneToMany(mappedBy = "playlist")  // 단방향 설정
    private List<YoutubeVideo> youtubeVideos = new ArrayList<>();

}