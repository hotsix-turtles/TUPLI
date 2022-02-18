package hotsixturtles.tupli.dto.simple;

import hotsixturtles.tupli.entity.meta.UserInfo;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class SimpleUserInfoDto {

    private Long id;

    private Long userSeq;

    private List<Long> playlistSeen;

    private Long watchTime;

    private Long boardUpload;

    private Long loginCount;

    private Long dailyCheck;

    private String dailyLoginYN;

    // 뱃지용 장르별 시간
    private Long watchTimeTrip; // 여행 1
    private Long watchTimeGame; // 게임 2
    private Long watchTimeLife; // 일상 3
    private Long watchTimeStyle; // 노하우/스타일 4
    private Long watchTimeAnimal; // 동물 5
    private Long watchTimeEntertainment; // 엔터테인먼트 6
    private Long watchTimeMovie; // 영화/드라마  7
    private Long watchTimeMusic; // 음악 8
    private Long watchTimeEducation; // 교육/시사 9
    private Long watchTimeSports; // 스포츠 10
    private Long watchTimeEtc; // 기타 11

    // 만든 플레이리스트 수
    private Long makePlaylist;

    // 만든 플레이룸 수
    private Long makePlayroom;

    private ConcurrentHashMap<String, Integer> TasteInfo = new ConcurrentHashMap<>();

    public SimpleUserInfoDto(UserInfo userInfo){
        this.id = userInfo.getId();
        this.userSeq = userInfo.getUserSeq();
        this.playlistSeen = userInfo.getPlaylistSeen();
        this.watchTime = userInfo.getWatchTime();

        this.boardUpload = userInfo.getBoardUpload();

        this.loginCount = userInfo.getLoginCount();

        this.dailyCheck = userInfo.getDailyCheck();

        this.dailyLoginYN = userInfo.getDailyLoginYN();

        // 뱃지용 장르별 시간
        this.watchTimeTrip = userInfo.getWatchTimeTrip(); // 여행 1
        this.watchTimeGame = userInfo.getWatchTimeGame(); // 게임 2
        this.watchTimeLife = userInfo.getWatchTimeLife(); // 일상 3
        this.watchTimeStyle = userInfo.getWatchTimeStyle(); // 노하우/스타일 4
        this.watchTimeAnimal = userInfo.getWatchTimeAnimal(); // 동물 5
        this.watchTimeEntertainment = userInfo.getWatchTimeEntertainment(); // 엔터테인먼트 6
        this.watchTimeMovie = userInfo.getWatchTimeMovie(); // 영화/드라마  7
        this.watchTimeMusic = userInfo.getWatchTimeMusic(); // 음악 8
        this.watchTimeEducation = userInfo.getWatchTimeEducation(); // 교육/시사 9
        this.watchTimeSports = userInfo.getWatchTimeSports(); // 스포츠 10
        this.watchTimeEtc = userInfo.getWatchTimeEtc(); // 기타 11

        // 만든 플레이리스트 수
        this.makePlaylist = userInfo.getMakePlaylist();

        // 만든 플레이룸 수
        this.makePlayroom = userInfo.getMakePlayroom();

        this.TasteInfo = userInfo.getTasteInfo();

    }

}
