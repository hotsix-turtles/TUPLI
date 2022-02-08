package hotsixturtles.tupli.dto.payments;

import lombok.Data;

import java.util.Date;

/**
 * 카카오페이 준비 완료, 다음 URL, 생성일자 등
 */
@Data
public class KakaoPayReadyVO {

    private String tid;
    private String next_redirect_pc_url;
    private Date created_at;

}

