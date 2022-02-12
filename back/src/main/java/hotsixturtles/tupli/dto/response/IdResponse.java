package hotsixturtles.tupli.dto.response;

import hotsixturtles.tupli.dto.simple.SimpleBadgeDto;
import hotsixturtles.tupli.entity.Badge;
import lombok.Data;

import java.util.List;

/**
 * 생성시 심플하게 Id만 반환
 */
@Data
public class IdResponse {

    private Long id;
    private List<SimpleBadgeDto> badgeResult;

    public IdResponse(Long id, List<SimpleBadgeDto> badgeResult) {
        this.id = id;
        this.badgeResult = badgeResult;
    }
}
