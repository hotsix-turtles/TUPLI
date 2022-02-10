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
    private Long watchTimeTrip = 0L; // 여행 1
    private Long watchTimeGame = 0L; // 게임 2
    private Long watchTimeLife = 0L; // 일상 3
    private Long watchTimeStyle = 0L; // 노하우/스타일 4
    private Long watchTimeAnimal = 0L; // 동물 5
    private Long watchTimeEntertainment = 0L; // 엔터테인먼트 6
    private Long watchTimeMovie = 0L; // 영화/드라마  7
    private Long watchTimeMusic = 0L; // 음악 8
    private Long watchTimeEducation = 0L; // 교육/시사 9
    private Long watchTimeSports = 0L; // 스포츠 10
    private Long watchTimeEtc = 0L; // 기타 11

    // 만든 플레이리스트 수
    private Long makePlaylist = 0L;

    // 만든 플레이룸 수
    private Long makePlayroom = 0L;

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
