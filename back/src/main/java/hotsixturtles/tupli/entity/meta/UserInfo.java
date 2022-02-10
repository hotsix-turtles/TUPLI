package hotsixturtles.tupli.entity.meta;

import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

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

    private Long loginCount = 0L;

    private Long dailyCheck = 0L;

    @Column(name = "DAILY_LOGIN_YN", length = 1)
//    @NotNull
    @Size(min = 1, max = 1)
    private String dailyLoginYN = "Y";


    // 뱃지용 장르별 시간
    private Long watchTimeTrip; // 여행
    private Long watchTimeGame; // 게임
    private Long watchTimeLife; // 일상
    private Long watchTimeStyle; // 노하우/스타일
    private Long watchTimeAnimal; // 동물
    private Long watchTimeEntertainment; // 엔터테인먼트
    private Long watchTimeMovie; // 영화/드라마
    private Long watchTimeMusic; // 음악
    private Long watchTimeEducation; // 교육/시사
    private Long watchTimeSports; // 스포츠
    private Long watchTimeEtc; // 기타

    // 유저 취향 분석용1
//    private Long TasteTrip; // 여행
//    private Long TasteGame; // 게임
//    private Long TasteLife; // 일상
//    private Long TasteStyle; // 노하우/스타일
//    private Long TasteAnimal; // 동물
//    private Long TasteEntertainment; // 엔터테인먼트
//    private Long TasteMovie; // 영화/드라마
//    private Long TasteMusic; // 음악
//    private Long TasteEducation; // 교육/시사
//    private Long TasteSports; // 스포츠
//    private Long TasteEtc; // 기타

    // 유저 취향 분석용2
    @Type(type = "json")
    @Column(columnDefinition = "json")
    private ConcurrentHashMap<String, Integer> TasteInfo = new ConcurrentHashMap<>();

}
