package hotsixturtles.tupli.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import hotsixturtles.tupli.entity.likes.PlayroomLikes;
import hotsixturtles.tupli.entity.youtube.YoutubeVideo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "playroom")
@Getter
@Setter
@NoArgsConstructor//(access = AccessLevel.PROTECTED)
@TypeDef(name = "json", typeClass = JsonType.class)
public class Playroom {

    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String content;
    private Boolean isPublic;
    private String tags;

//    @Type(type = "json")
//    @Column(columnDefinition = "json")
//    private List<Long> playlists;  // request에서 번호만 추출해서 저장

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private ConcurrentHashMap<Long, List<Long>> playlists;

    private OffsetDateTime startTime;

    @PrePersist
    private void beforeSaving() {
        startTime = OffsetDateTime.now();
    }

    private OffsetDateTime endTime;

    // 유튜브 Tag 등의 메타정보 조합, 뱃지에 사용
    // 특정방에 있었다는 정보만 있으면 조합 가능하니까 DTO로 보낼 필요 없음
    @Type(type = "json")
    @Column(columnDefinition = "json")
    private ConcurrentHashMap<Integer, Integer> playroomInfo;


    // 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;  // 방제작자, 방장, 싱크조절자.

    @OneToMany(mappedBy = "playroom")
    private List<PlayroomLikes> playroomLikes = new ArrayList<>();

    @OneToMany(mappedBy = "playroom", cascade = {CascadeType.ALL})
    private List<YoutubeVideo> videos = new ArrayList<>();

    // 기타 : DTO 외 내부 추천 및 뱃지용
    private Integer userCount;


}
