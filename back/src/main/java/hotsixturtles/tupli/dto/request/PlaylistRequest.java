package hotsixturtles.tupli.dto.request;

import hotsixturtles.tupli.dto.simple.SimpleYoutubeVideoDto;
import lombok.Data;

import java.util.List;

@Data
public class PlaylistRequest {

    private String title;
    private String content;
    private String tags;
    private Boolean isPublic;
    private List<SimpleYoutubeVideoDto> videos;

}
