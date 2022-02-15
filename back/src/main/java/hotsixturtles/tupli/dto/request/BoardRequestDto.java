package hotsixturtles.tupli.dto.request;

import lombok.Data;

@Data
public class BoardRequestDto {

    private String title;
    private String content;
    private String type; // null, "playlist", "playroom"
    private Long typeId; // 대상 type의 Id

}
