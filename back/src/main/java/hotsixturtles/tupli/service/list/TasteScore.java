package hotsixturtles.tupli.service.list;

public class TasteScore {

    // 플레이리스트 제작 : 영상 종류당 3점
    public static final Integer SCORE_PLAYLIST_MAKE = 3;
    // 플레이룸 제작 : 영상 종류당 2점
    public static final Integer SCORE_PLAYROOM_MAKE = 2;
    // 영상에 좋아요 : 해당 영상 종류에 4점
    public static final Integer SCORE_VIDEO_LIKE = 4;
    // 영상에 좋아요취소 : 해당 영상 종류에 -3점 (음수가 되지 않게 주의)
    public static final Integer SCORE_VIDEO_UNLIKE = -3;
    // 플레이리스트 좋아요하기 : 해당 영상 종류당 1점
    public static final Integer SCORE_PLAYLIST_LIKE = 1;
    // 플레이룸 좋아요하기 : 해당 영상 종류당 2점
    public static final Integer SCORE_PLAYROOM_LIKE = 2;
    // 플레이룸 방문 : 해당 영상 종류당 1점
    public static final Integer SCORE_PLAYROOM_VISIT = 2;




}
