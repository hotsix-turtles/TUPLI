package hotsixturtles.tupli.dto;

import hotsixturtles.tupli.dto.simple.SimpleUserDto;
import hotsixturtles.tupli.entity.Playroom;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class PlayroomDto {

    private Long id;

    @Size(max = 200)
    private String roomTitle;

    private String roomContent;

    private Boolean roomPublic;

    // 일단 주석
//    @Type(type = "json")
//    @Column(columnDefinition = "json")
//    private List<Long> inviteIds;

    private List<Long> roomPlaylists;

    private List<String> roomTags;

    private List<Long> roomVideos;

    private OffsetDateTime roomStartTime;

    private OffsetDateTime roomEndTime;

    private SimpleUserDto user;

    public PlayroomDto(Playroom playroom) {
        this.id = playroom.getId();
        this.roomTitle = playroom.getRoomTitle();
        this.roomContent = playroom.getRoomContent();
        this.roomPublic = playroom.getRoomPublic();
//        this.inviteIds = playroom.getInviteIds();
        this.roomPlaylists = playroom.getRoomPlaylists();
        this.roomTags = playroom.getRoomTags();
        this.roomVideos = playroom.getRoomVideos();
        this.roomStartTime = playroom.getRoomStartTime();
        this.roomEndTime = playroom.getRoomEndTime();

        this.user = new SimpleUserDto(playroom.getUser());
    }
}
