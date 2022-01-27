package hotsixturtles.tupli.dto.params;

import lombok.Data;

@Data
public class UserSearchCondition {
    private String keyword;
    private String email;
}
