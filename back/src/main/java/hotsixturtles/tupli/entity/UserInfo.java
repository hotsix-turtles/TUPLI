package hotsixturtles.tupli.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;

/**
 * 타 유저도 조회하거나, 분리하는 게 유리한 유저 정보
 */
@Entity
@Table(name = "user_info")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "json", typeClass = JsonType.class)
public class UserInfo {

    @Id @GeneratedValue
    private Long id;

    private Long userSeq;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private List<Long> playlistSeen;

    // 게시글 수 등의 뱃지에 이용할 컬럼 이곳에 추가...
    @Column(name = "WATCHING_TIME")
    private Long watchTime = 0L;

    private Long boardUpload = 0L;

}
