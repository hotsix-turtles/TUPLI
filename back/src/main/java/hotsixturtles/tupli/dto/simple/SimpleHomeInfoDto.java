package hotsixturtles.tupli.dto.simple;

import hotsixturtles.tupli.entity.Board;
import hotsixturtles.tupli.entity.HomeInfo;
import hotsixturtles.tupli.entity.Playlist;
import hotsixturtles.tupli.entity.Playroom;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class SimpleHomeInfoDto {

    private Long id;

    private String type;

    // Playroom, Playlist, BoardId 저장
    private Long infoId;

    private OffsetDateTime createdAt;


    public SimpleHomeInfoDto(HomeInfo homeInfo){
        this.id = homeInfo.getId();
        this.type = homeInfo.getType();
        this.infoId = homeInfo.getInfoId();
        this.createdAt = homeInfo.getCreatedAt();
    }

    public SimpleHomeInfoDto(Playlist playlist){
        this.type = "playlist";
        this.infoId = playlist.getId();
    }

    public SimpleHomeInfoDto(Playroom playroom){
        this.type = "playroom";
        this.infoId = playroom.getId();
    }

    public SimpleHomeInfoDto(Board board){
        this.type = "board";
        this.infoId = board.getId();
    }
}
