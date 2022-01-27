package hotsixturtles.tupli.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import hotsixturtles.tupli.dto.PlayroomDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "playroom")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@TypeDef(name = "json", typeClass = JsonType.class)
public class Playroom {

    @Id
    @GeneratedValue
    private Long id;

    @Size(max = 200)
    private String roomTitle;

    private String roomContent;

    private Boolean roomPublic;

//    @Type(type = "json")
//    @Column(columnDefinition = "json")
//    private List<Long> inviteIds;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private List<Long> roomPlaylists;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private List<String> roomTags;


    @Type(type = "json")
    @Column(columnDefinition = "json")
    private List<Long> roomVideos;

    @Column(name = "start_time")
    private OffsetDateTime roomStartTime;

    @PrePersist
    private void beforeSaving() {
        roomStartTime = OffsetDateTime.now();
    }
    @Column(name = "end_time")
    private OffsetDateTime roomEndTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Integer userCount;


    public Playroom(PlayroomDto playroomDto, User user) {
        this.id = playroomDto.getId();
        this.roomTitle = playroomDto.getRoomTitle();
        this.roomContent = playroomDto.getRoomContent();
        this.roomPublic = playroomDto.getRoomPublic();
//        this.roominviteIds = playroomDto.getInviteIds();
        this.roomPlaylists = playroomDto.getRoomPlaylists();
        this.roomTags = playroomDto.getRoomTags();
        this.roomVideos = playroomDto.getRoomVideos();
//        this.startTime = playroomDto.getRoomStartTime();
        this.roomEndTime = playroomDto.getRoomEndTime();
        this.user = user;
        this.userCount = 0;

    }

}
