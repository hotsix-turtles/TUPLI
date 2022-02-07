package hotsixturtles.tupli.dto.payments;

import lombok.Data;

import java.util.Date;

/**
 * 카카오페이 승인을 위한 기본 VO
 */
@Data
public class KakaoPayApprovalVO {

    private String aid, tid, cid, sid;
    private String partner_order_id, partner_user_id, payment_method_type;
    private AmountVO amount;
    private CardVO card_info;
    private String item_name, item_code, payload;
    private Integer quantity, tax_free_amount, vat_amount;
    private Date created_at, approved_at;
}

/**
 * 카카오페이 관련 총액, 세금 등
 */
@Data
class AmountVO {
    private Integer total, tax_free, vat, point, discount;
}

/**
 * 카카오페이 카드정보
 */
@Data
class CardVO {
    private String purchase_corp, purchase_corp_code;
    private String issuer_corp, issuer_corp_code;
    private String bin, card_type, install_month, approved_id, card_mid;
    private String interest_free_install, card_item_code;
}