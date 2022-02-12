package hotsixturtles.tupli.dto.simple;

import hotsixturtles.tupli.entity.Badge;
import lombok.Data;

@Data
public class SimpleBadgeDto {

    private Long badgeSeq;

    private String badgeName;

    public SimpleBadgeDto(Badge badge){
        this.badgeSeq = badge.getBadgeSeq();
        this.badgeName = badge.getBadgeName();
    }
}
