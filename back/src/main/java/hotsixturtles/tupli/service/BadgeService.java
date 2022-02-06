package hotsixturtles.tupli.service;

import hotsixturtles.tupli.entity.Badge;
import hotsixturtles.tupli.entity.UserBadge;
import hotsixturtles.tupli.entity.likes.UserLikes;
import hotsixturtles.tupli.entity.meta.UserInfo;
import hotsixturtles.tupli.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BadgeService {


    private static final Integer Badge1_increment = 3;
    private static final List<Long> Badge1List = Arrays.asList(10L, 20L, 30L);
    private static final Integer badgeNum = 34; // 전체 뱃지 숫자
    private static final Integer Badge2_increment = 6;
    private static final List<Long> Badge2List = Arrays.asList(2L, 3L, 4L);
    private static final Integer Badge3_increment = 7;
    private static final Integer Badge4_increment = 10;
    private static final List<Long> Badge4List = Arrays.asList(2L, 3L, 4L);
    private static final Integer Badge5_increment = 13;
    private static final List<Long> Badge5List = Arrays.asList(2L, 3L, 4L);
    private static final Integer Badge6_increment = 16;
    private static final List<Long> Badge6List = Arrays.asList(2L, 3L, 4L);
    private static final Integer Badge7_increment = 19;
    private static final List<Long> Badge7List = Arrays.asList(2L, 3L, 4L);
    private static final Integer Badge8_increment = 22;
    private static final List<Long> Badge8List = Arrays.asList(2L, 3L, 4L);
    private static final Integer Badge9_increment = 25;
    private static final List<Long> Badge9List = Arrays.asList(2L, 3L, 4L);
    private static final Integer Badge10_increment = 28;
    private static final List<Long> Badge10List = Arrays.asList(2L, 3L, 4L);
    private static final Integer Badge11_increment = 31;
    private static final List<Long> Badge11List = Arrays.asList(2L, 3L, 4L);
    private static final Integer Badge12_increment = 34;
    private static final List<Long> Badge12List = Arrays.asList(2L, 3L, 4L);

//    private static final Long Badge1_grad3 = 1000;

    private final PlaylistRepository playlistRepository;

    private final UserRepository userRepository;

    private final BadgeRepository badgeRepository;

    private final UserBadgeRepository userBadgeRepository;

    private final UserInfoRepository userInfoRepository;

    private final UserLikesRepository userlikesRepository;

    private final PlayroomRepository playroomRepository;
//
//    @Transactional
//    public List<Badge> addBadge(Map<String, String> badgeInfo){
//        Long badgeSeq = Long.parseLong(badgeInfo.get("badgeSeq"));
//        String badgeColumnName = badgeInfo.get("badgeColName");
//        Long badgeValue = Long.parseLong(badgeInfo.get("badgeValue"));
//        Badge badge= badgeRepository.findByBadgeSeq(badgeSeq);
//
//    }

    public List<UserBadge> getBadgeList(Long userSeq){
        List<UserBadge> badgeList = userBadgeRepository.findAllByUserSeq(userSeq);
//        List<Badge> result = new ArrayList<>();
//        for(UserBadge userBadge : badgeList){
//            result.add(badgeRepository.findByBadgeSeq(userBadge.getBadgeSeq()));
//        }
        return badgeList;
    }

    public List<Long> getUserBadgeSeq(List<UserBadge> userBadges){
        List<Long> badges = new ArrayList<Long>();

        int len = userBadges.size();
        int i = 0;
        for(i = 0 ; i < len; i++){
            badges.add(userBadges.get(i).getBadgeSeq());
        }

        return badges;
    }

    @Transactional
    public void deleteBadges(Long userSeq){
        userBadgeRepository.deleteAllByUserSeq(userSeq);
    }

    @Transactional
    public List<Badge> updateBadgeSpecific(Long userSeq, Long badgeSeq){

        List<Badge> badgeList = new ArrayList<Badge>();
        List<Long> badges = new ArrayList<Long>();

        if(badgeSeq == 1) {
            Long nowTime = userInfoRepository.findById(userSeq).get().getWatchTime();
            for (int i = 1; i <= Badge1_increment; i++) {
                if (nowTime >= Badge1List.get(i-1)) {
                    UserBadge userBadge = userBadgeRepository.findByBadgeSeqAndUserSeq(1L * i, userSeq);
                    if (userBadge == null) {
                        userBadge.setBadgeSeq(badgeSeq);
                        userBadge.setUserSeq(userSeq);
                        userBadgeRepository.save(userBadge);
                        badgeList.add(badgeRepository.findByBadgeSeq(badgeSeq));
                    }
                }
            }
        }
        return badgeList;
    }


    // 프로필 들어가면 뱃지 업데이트
    @Transactional
    public List<UserBadge> updateBadge(Long userSeq) {
        List<UserBadge> userBadges = userBadgeRepository.findAllByUserSeq(userSeq);
        List<Long> badges = new ArrayList<Long>();
        List<UserBadge> updateBadgeList = new ArrayList<>();

        int len = userBadges.size();
        int i = 0;
        for(i = 0 ; i < len; i++){
            badges.add(userBadges.get(i).getBadgeSeq());
        }
        i = 1;

        // 모든 경우 다 파악
        while(i <= badgeNum){

            // 시청 시간 파악
            if(i == 1){
                i = checkWatchTime(i, userSeq, badges);
            }
            // 게시한 게시글 수 파악
            else if(i == 4){
                i = checkBoardUpload(i, userSeq, badges);
            }
            // 프리미엄뱃지
            else if(i == 7){
                i = checkPremium(i, userSeq, badges);
            }
            // 좋아요 파악
            else if(i == 8){
                i = checkPlaylistLikes(i, userSeq, badges);
            }
            // 팔로우 하는
            else if(i == 11){
                i = checkFollowees(i, userSeq, badges);
            }
            // 팔로우 받는
            else if(i == 14){
                i = checkFollowers(i, userSeq, badges);
            }
            // 만든 플레이리스트
            else if(i == 17){
                i = checkPlaylistMake(i, userSeq, badges);
            }
            // 만든 플레이룸
            else if(i == 20){
                i = checkPlayroomMake(i, userSeq, badges);
            }
            // 로그인 횟수
            else if(i == 23){
                i = checkLoginNum(i, userSeq, badges);
            }
            // 출석 일수
            else if(i == 26){
                i = checkDaily(i, userSeq, badges);
            }
            // 플레이룸 최고 인원수
            else if(i == 29){
                i = checkPlayroomMaxUsers(i, userSeq, badges);
            }
            // 호러 장르 시청( 각 장르 ) 장르마다 시청 시간 저장해야하나??
            // 일단 보류...
            else if(i == 32){
                i = 50; // while 빠져나가기위함 (test용)
            }
            //i++관련 넣어서 여기다 넣어줘서 나중에 수정 용이하게 하자....
        }

        return updateBadgeList;
    }

    // 시청 시간 파악
    public int checkWatchTime(int i, Long userSeq, List<Long> badges){
        Long nowTime = userInfoRepository.findOneByUserSeq(userSeq).getWatchTime();
        for (i = 1; i <= Badge1_increment; i++) {
            if (nowTime >= Badge1List.get(i-1)) {
                if(!badges.contains(Long.valueOf(i))){
                    UserBadge userBadge = new UserBadge(null, userSeq, Long.valueOf(i), OffsetDateTime.now());
                    userBadgeRepository.save(userBadge);
                }
            }
            else {
                return 4;
            }
        }
        return i;
    }

    // 게시한 게시글 수 파악
    public int checkBoardUpload(int i, Long userSeq, List<Long> badges){
        Long boardUploadNum = userInfoRepository.findOneByUserSeq(userSeq).getBoardUpload();
        for(i = 4; i <= Badge2_increment; i++){
            if(boardUploadNum >= Badge2List.get(i-4)){
                if(!badges.contains(Long.valueOf(i))){
                    UserBadge userBadge = new UserBadge(null, userSeq, Long.valueOf(i), OffsetDateTime.now());
                    userBadgeRepository.save(userBadge);
                }
            }
            else {
                return 7;
            }
        }
        return i;
    }

    // 프리미엄 유저 확인
    public int checkPremium(int i, Long userSeq, List<Long> badges){
        if(userRepository.findByUserSeq(userSeq).getIs_vip().equals("Y")) {
            if(!badges.contains(Long.valueOf(i))){
                UserBadge userBadge = new UserBadge(null, userSeq, Long.valueOf(i), OffsetDateTime.now());
                userBadgeRepository.save(userBadge);
            }
        }
        i++;
        return i;
    }

    // 좋아요 수 확인 ( 해당 유저 플레이 리스트 )
    public int checkPlaylistLikes(int i, Long userSeq, List<Long> badges){
        Long playlistLikes = playlistRepository.findPlaylistLikes(userSeq);
        for(; i <= Badge4_increment; i++) {
            if(playlistLikes >= Badge4List.get(i-8)){
                if(!badges.contains(Long.valueOf(i))){
                    UserBadge userBadge = new UserBadge(null, userSeq, Long.valueOf(i), OffsetDateTime.now());
                    userBadgeRepository.save(userBadge);
                }
            }
            else {
                return 11;
            }
        }
        return i;
    }

    // 자신이 팔로우 한 사람 수
    public int checkFollowees(int i, Long userSeq, List<Long> badges){
        List<UserLikes> userLikesList = userlikesRepository.findByFromUser(userSeq);
        Long followCount = Long.valueOf(userLikesList.size());
        for(; i <= Badge5_increment; i++){
            if(followCount >= Badge5List.get(i-11)){
                if(!badges.contains(Long.valueOf(i))){
                    UserBadge userBadge = new UserBadge(null, userSeq, Long.valueOf(i), OffsetDateTime.now());
                    userBadgeRepository.save(userBadge);
                }
            }
            else {
                return 14;
            }
        }
        return i;
    }

    // 자신을 팔로우 한 사람 수
    public int checkFollowers(int i, Long userSeq, List<Long> badges){
        List<UserLikes> userLikesList = userlikesRepository.findFollowers(userSeq);
        Long followerCount = Long.valueOf(userLikesList.size());
        for(; i <= Badge6_increment; i++){
            if(followerCount >= Badge6List.get(i-14)){
                if(!badges.contains(Long.valueOf(i))){
                    UserBadge userBadge = new UserBadge(null, userSeq, Long.valueOf(i), OffsetDateTime.now());
                    userBadgeRepository.save(userBadge);
                }
            }
            else {
                return 17;
            }
        }
        return i;
    }

    // 만든 플레이리스트 수
    public int checkPlaylistMake(int i, Long userSeq, List<Long> badges){
        Long playlistCount = playlistRepository.findCountByUser(userSeq);
        for(i = 17; i <= Badge7_increment; i++){
            if(playlistCount >= Badge7List.get(i-17)){
                if(!badges.contains(Long.valueOf(i))){
                    UserBadge userBadge = new UserBadge(null, userSeq, Long.valueOf(i), OffsetDateTime.now());
                    userBadgeRepository.save(userBadge);
                }
            }
            else {
                return 20;
            }
        }
        return i;
    }

    // 만든 플레이룸 수
    public int checkPlayroomMake(int i, Long userSeq, List<Long> badges){
        Long playroomCount = playroomRepository.findCountByUser(userSeq);
        for(i = 20; i <= Badge8_increment; i++){
            if(playroomCount >= Badge8List.get(i-20)){
                if(!badges.contains(Long.valueOf(i))){
                    UserBadge userBadge = new UserBadge(null, userSeq, Long.valueOf(i), OffsetDateTime.now());
                    userBadgeRepository.save(userBadge);
                }
            }
            else {
                return 23;
            }
        }
        return i;
    }

    // 로그인 횟수
    public int checkLoginNum(int i, Long userSeq, List<Long> badges){
        UserInfo nowUserInfo = userInfoRepository.findOneByUserSeq(userSeq);
        Long loginCount = nowUserInfo.getLoginCount();
        for(i = 23; i <= Badge9_increment; i++){
            if(loginCount >= Badge9List.get(i-23)){
                if(!badges.contains(Long.valueOf(i))){
                    UserBadge userBadge = new UserBadge(null, userSeq, Long.valueOf(i), OffsetDateTime.now());
                    userBadgeRepository.save(userBadge);
                }
            }
            else {
                return 26;
            }
        }
        return i;
    }

    // 출석 일 수
    public int checkDaily(int i, Long userSeq, List<Long> badges){
        UserInfo nowUserInfo = userInfoRepository.findOneByUserSeq(userSeq);
        Long dailyCount = nowUserInfo.getDailyCheck();
        for(i = 26; i <= Badge10_increment; i++){
            if(dailyCount >= Badge10List.get(i-26)){
                if(!badges.contains(Long.valueOf(i))){
                    UserBadge userBadge = new UserBadge(null, userSeq, Long.valueOf(i), OffsetDateTime.now());
                    userBadgeRepository.save(userBadge);
                }
            }
            else {
                return 29;
            }
        }
        return i;
    }

    // 플레이룸 최고 인원 수
    public int checkPlayroomMaxUsers(int i, Long userSeq, List<Long> badges){
        Long maxUser = playroomRepository.findRoomMaxUser(userSeq);
        for(i = 29; i <= Badge11_increment; i++){
            if(maxUser >= Badge11List.get(i-29)){
                if(!badges.contains(Long.valueOf(i))){
                    UserBadge userBadge = new UserBadge(null, userSeq, Long.valueOf(i), OffsetDateTime.now());
                    userBadgeRepository.save(userBadge);
                }
            }
            else {
                return 32;
            }
        }
        return i;
    }



}
