package hotsixturtles.tupli.dto.param;

import lombok.Data;

/**
 * 왠만한 검색에서 공통으로 쓸 것 같은 Condition
 */
@Data
public class SimpleCondition {

    private String type; // 최신순, 관련순, 좋아요순
    private String keyword;

}
