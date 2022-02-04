package hotsixturtles.tupli.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import hotsixturtles.tupli.entity.likes.PlaylistLikes;
<<<<<<< HEAD
=======
import hotsixturtles.tupli.entity.meta.PlaylistInfo;
>>>>>>> 2e271a7bd18cd87da6b2056108ea373197c120d5
import hotsixturtles.tupli.entity.youtube.YoutubeVideo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
<<<<<<< HEAD
=======
import java.time.LocalDateTime;
>>>>>>> 2e271a7bd18cd87da6b2056108ea373197c120d5
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

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

    private String title;
    private String content;
    private String tags;
    private Boolean isPublic;

    private String image;  // 플레이리스트 자체에 이미지 넣을지, 영상 썸네일 중 하나일지?

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private List<Long> recommendPlaylists;  // 이 플레이리스트 기준, 추천 플레이리스트

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
    private ConcurrentHashMap<Integer, Integer> playlistInfo;  // 유튜브 Tag + Custom Tag 리스트 등의 메타정보 조합

    // 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "playlist")
    private List<PlaylistLikes> playlistLikes = new ArrayList<>();

    @OneToMany(mappedBy = "playlist")  // 단방향 설정
    private List<YoutubeVideo> youtubeVideos = new ArrayList<>();

}