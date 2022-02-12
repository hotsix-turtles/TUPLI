package hotsixturtles.tupli.dto;

import hotsixturtles.tupli.dto.simple.SimpleUserDto;
import hotsixturtles.tupli.entity.Playroom;
import hotsixturtles.tupli.entity.likes.PlayroomLikes;
import lombok.Data;

@Data
public class PlayroomLikesDto {
    private Long id;
    private PlayroomDto playroom;
    private SimpleUserDto user;

    public PlayroomLikesDto(PlayroomLikes playroomLikes){
        this.id = playroomLikes.getId();
        this.playroom = new PlayroomDto(playroomLikes.getPlayroom());
        this.user = new SimpleUserDto(playroomLikes.getUser());
    }
}
