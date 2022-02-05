package hotsixturtles.tupli.dto.response;

import lombok.Data;

/**
 * 생성시 심플하게 Id만 반환
 */
@Data
public class IdResponse {

    private Long id;

    public IdResponse(Long id) {
        this.id = id;
    }
}
