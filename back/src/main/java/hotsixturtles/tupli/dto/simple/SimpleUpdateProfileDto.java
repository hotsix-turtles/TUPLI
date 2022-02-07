package hotsixturtles.tupli.dto.simple;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SimpleUpdateProfileDto {

    private String introduction;
    private String nickname;
    private String image;
}
