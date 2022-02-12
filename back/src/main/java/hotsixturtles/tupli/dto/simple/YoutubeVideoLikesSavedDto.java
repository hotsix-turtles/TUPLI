package hotsixturtles.tupli.dto.simple;

import lombok.Data;

import java.util.List;

@Data
public class YoutubeVideoLikesSavedDto {

    List<Boolean> isLikesList;
    List<Integer> isLikesCnt;
    List<Boolean> isSavedList;

}
