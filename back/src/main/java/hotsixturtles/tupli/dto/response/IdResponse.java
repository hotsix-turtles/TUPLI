package hotsixturtles.tupli.dto.response;

import hotsixturtles.tupli.entity.Badge;
import lombok.Data;

import java.util.List;

/**
 * 생성시 심플하게 Id만 반환
 */
@Data
public class IdResponse {

    private Long id;
    private List<Badge> badgeResult;

    public IdResponse(Long id, List<Badge> badgeResult) {
        this.id = id;
        this.badgeResult = badgeResult;
    }
}
