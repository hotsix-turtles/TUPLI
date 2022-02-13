package hotsixturtles.tupli.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hotsixturtles.tupli.dto.PlayroomDto;
import hotsixturtles.tupli.dto.request.RequestPlayroomDto;
import hotsixturtles.tupli.dto.simple.SimpleHomeInfoDto;
import hotsixturtles.tupli.dto.simple.SimpleYoutubeVideoDto;
import hotsixturtles.tupli.entity.*;
import hotsixturtles.tupli.entity.likes.PlayroomLikes;
import hotsixturtles.tupli.entity.likes.UserLikes;
import hotsixturtles.tupli.entity.meta.UserInfo;
import hotsixturtles.tupli.entity.youtube.YoutubeVideo;
import hotsixturtles.tupli.repository.*;
import hotsixturtles.tupli.repository.likes.PlayroomLikesRepository;
import hotsixturtles.tupli.service.list.CategoryList;
import hotsixturtles.tupli.service.list.TasteScore;
import hotsixturtles.tupli.utils.TasteUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static hotsixturtles.tupli.entity.QPlaylist.playlist;
import static hotsixturtles.tupli.entity.QPlayroom.*;
import static hotsixturtles.tupli.entity.QUser.user;
import static org.springframework.util.StringUtils.hasText;

@Service
@RequiredArgsConstructor
public class PlayroomService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final UserInfoRepository userInfoRepository;
    private final PlayroomRepository playroomRepository;
    private final YoutubeVideoRepository youtubeVideoRepository;
    private final NotificationService notificationService;
    private final PlayroomRepositoryCustom playroomRepositoryCustom;
    private final PlayroomLikesRepository playroomLikesRepository;
    private final CategoryRepository categoryRepository;
    private final HomeInfoRepository homeInfoRepository;

    // 심플 querydsl
    private final JPAQueryFactory jpaQueryFactory;

    public List<Playroom> getPlayroomList(){

        // Paging 조건 정해서 추가
        return playroomRepository.findAll();
    }

    // Home 화면 Playroom 검색
    public List<Playroom> getHomePlayrooms(Pageable pageable) {
        return playroomRepositoryCustom.listByHomePlayroom(pageable);
    }

    public Playroom getPlayroom(Long playroomId){

        // Paging 조건 정해서 추가
        Playroom playroom = playroomRepository.findById(playroomId).orElse(null);
        return playroom;
//        return playroomRepository.findById(playroomId).orElse(null);
    }

    // 사용자가 좋아요 누른 playroom
    public List<Playroom> getLikedPlayrooms(Long userSeq){
        return playroomRepository.findLikedPlayrooms(userSeq);
    }

    @Transactional
    public PlayroomDto addPlayroom(RequestPlayroomDto playroomDto, Long userSeq){

        Playroom playroom = new Playroom();

        // 임시명칭 : 방 생성자(maker), 싱크 조절자(owner, 방장) << 현재 분리안은 고도화로 미룸
        User maker = userRepository.findByUserSeq(userSeq);
        playroom.setUser(maker);

        // 유저 취향 가져오기
        UserInfo userInfo = userInfoRepository.findOneByUserSeq(userSeq);
        ConcurrentHashMap<String, Integer> tasteInfo = userInfo.getTasteInfo();

        playroom.setTitle(playroomDto.getTitle());
        playroom.setContent(playroomDto.getContent());
        playroom.setIsPublic(playroomDto.getIsPublic());
        playroom.setTags(playroomDto.getTags());
        playroom.setEndTime(playroomDto.getEndTime());
        playroom.setStartTime(playroomDto.getStartTime());

        // 플레이리스트 비디오 분리하고 저장 + ID만 저장
        ConcurrentHashMap<Long, List<Long>> playlists = new ConcurrentHashMap<>();
        ConcurrentHashMap<Integer, Integer> playroomInfo = new ConcurrentHashMap<Integer, Integer>();
        List<YoutubeVideo> youtubeVideoList = new ArrayList<>();
        for (Map.Entry<Long, List<String>> entry : playroomDto.getPlaylists().entrySet()) {
            // 플레이리스트 ID만 저장
//            playlists.add(entry.getKey());
            List<Long> playroomPlList = new ArrayList<>();


            String image = null;

            // 비디오 저장(플레이리스트 삭제 대비 및 편한 추가 삭제를 위해 DB 별도 저장) 최적화 반드시 필요!!!!! $$$
            for (String videoUrl : entry.getValue()) {
                YoutubeVideo video = new YoutubeVideo();
                YoutubeVideo existVideo = youtubeVideoRepository.findFirstByVideoId(videoUrl);
                // 첫 영상 이미지만 저장 (미리보기용)
                if (image == null) {
                    image = existVideo.getThumbnail();
                    playroom.setImage(image);
                }
                video.setPlayroom(playroom);
                video.copyVideo(existVideo);
                youtubeVideoRepository.save(video);
                YoutubeVideo nowVideo = youtubeVideoRepository.findFirstByVideoIdOrderByIdDesc(videoUrl);
                playroomPlList.add(nowVideo.getId());
                youtubeVideoList.add(nowVideo);

                // 플레이룸 구성 비디오 정보로 메타 정보 구축
                Integer categoryId = existVideo.getCategoryId();
                Integer count = playroomInfo.getOrDefault(categoryId, 0);
                playroomInfo.put(categoryId, count+1);

                // 카테고리에 따른 분류
                String category = CategoryList.CATEGORY_LIST.getOrDefault(categoryId, "기타");

                // 취향 반영
                Integer tasteScore = tasteInfo.getOrDefault(category, 0);
                tasteInfo.put(category, tasteScore + TasteScore.SCORE_PLAYROOM_MAKE);
            }
            playlists.put(entry.getKey(), playroomPlList);

        }
        playroom.setPlayroomInfo(playroomInfo);
        playroom.setPlaylists(playlists);

        List<SimpleYoutubeVideoDto> youtubeVideoDtoList = youtubeVideoList
                .stream().map(x -> new SimpleYoutubeVideoDto(x)).collect(Collectors.toList());
        playroomDto.setVideos(youtubeVideoDtoList);

        // 카테고리 정보
        Map<Integer, String> categoryList = new HashMap<>();
        List<Category> categories =  categoryRepository.findAll();
        for(Category category : categories){
            categoryList.put(category.getCategoryId().intValue(), category.getSort());
        }

        Set<String> categorys = new HashSet<>();

        // Video 정보
        ConcurrentHashMap<Integer, Integer> playroominfo = new ConcurrentHashMap<Integer, Integer>();
        String image = null;
        for (SimpleYoutubeVideoDto videoDto : playroomDto.getVideos()) {
            // 첫 영상 이미지만 저장 (미리보기용)
            if (image == null) {
                image = videoDto.getThumbnail();
                playroom.setImage(image);
            }

            // Playroominfo에 따라 갈림
            Integer categoryId = videoDto.getCategoryId();
            Integer count = playroominfo.getOrDefault(categoryId, 0);
            playroominfo.put(categoryId, count+1);

            // 카테고리에 따른 분류
            String category = categoryList.getOrDefault(categoryId, "기타");
            categorys.add(category);
        }

        // 검색을 위한 Stringify
        String categorysString = "";
        for (String category : categorys) {
            categorysString = categorysString + category + ", ";
        }

        playroom.setPlayroomCate(categorysString);
        Playroom nowPlayroom = playroomRepository.save(playroom);

        // 플레이룸 개설 알림 보내기 (초청 유저)
        for (Long inviteId : playroomDto.getInviteIds()) {
            notificationService.notiPlayroomMake(userSeq, inviteId);
        }

        // 플레이룸 개설 알림 보내기 (로직짜고, 팔로우한 유저들에게, 유저 설정 부분 만들어지면 그 때 수정)
        if (false) {
            List<UserLikes> followers = userService.getFollowers(userSeq);
            for (UserLikes follower : followers) {
                notificationService.notiInvite(userSeq, follower.getFromUser().getUserSeq());
            }

        }

        // 유저 정보 저장
        userInfo.setTasteInfo(tasteInfo);
        userInfoRepository.save(userInfo);

        // 유저 취향 분석 후 저장
        List<String> userTaste = TasteUtil.getTaste(tasteInfo);
        maker.setTaste(userTaste);
        userRepository.save(maker);

        HomeInfo homeInfo = new HomeInfo();
        homeInfo.setType("playroom");
        homeInfo.setInfoId(nowPlayroom.getId());
        homeInfo.setUserSeq(userSeq);
        homeInfoRepository.save(homeInfo);

        return new PlayroomDto(playroom);
    }

    @Transactional
    public PlayroomDto updatePlayroom(Long playroomId, RequestPlayroomDto playroomDto, Long userSeq){

        Playroom playroom = playroomRepository.findById(playroomId).orElse(null);

        if(playroom == null || playroom.getUser().getUserSeq() != userSeq){
            return null;
        }

        if(playroomDto.getTitle() != null){
            playroom.setTitle(playroomDto.getTitle());
        }
        if(playroomDto.getContent() != null){
            playroom.setContent(playroomDto.getContent());
        }


        playroomRepository.save(playroom);

        return new PlayroomDto(playroom);
    }

    @Transactional
    public Playroom deletePlayroom(Long playroomId, Long userSeq){

        Playroom playroom = playroomRepository.findById(playroomId).orElse(null);

        if(playroom == null || playroom.getUser().getUserSeq() != userSeq){
            return null;
        }

        playroomRepository.deleteById(playroomId);
        homeInfoRepository.deleteByInfoId(playroomId);
        return playroom;
    }

    @Transactional
    public void changePlayroomUser(Long userSeq, Long playroomId) {
        Playroom playroom = playroomRepository.findById(playroomId).orElse(null);
        if (playroom != null) {
            playroom.setUser(userRepository.findByUserSeq(userSeq));
            playroomRepository.save(playroom);
        }
    }

    public PlayroomLikes getPlayroomLike(Long userSeq, Long playroomId) {
        return playroomLikesRepository.findExist(userSeq, playroomId);
    }

    @Transactional
    public void addPlayroomLike(Long userSeq, Long playroomId){
        PlayroomLikes playroomlike = playroomLikesRepository.findExist(userSeq, playroomId);
        if(playroomlike == null) {
            User user = userRepository.findByUserSeq(userSeq);

            PlayroomLikes playroomLikes = new PlayroomLikes();
            playroomLikes.setPlayroom(playroomRepository.findById(playroomId).orElse(null));
            playroomLikes.setUser(user);
            playroomLikesRepository.save(playroomLikes);

            // 한길 : playroom 에 좋아요 눌렸을 때 +1 하기
            Playroom playroom = playroomRepository.getById(playroomId);
            playroom.setLikesCnt(playroom.getLikesCnt()+1);
            playroomRepository.save(playroom);

            // 취향분석
            // 유저 취향 가져오기
            UserInfo userInfo = userInfoRepository.findOneByUserSeq(userSeq);
            ConcurrentHashMap<String, Integer> tasteInfo = userInfo.getTasteInfo();
            for (YoutubeVideo youtubeVideo : playroom.getVideos()) {
                // 카테고리에 따른 분류
                String category = CategoryList.CATEGORY_LIST.getOrDefault(youtubeVideo.getCategoryId(), "기타");
                // 취향 반영
                Integer tasteScore = tasteInfo.getOrDefault(category, 0);
                tasteInfo.put(category, tasteScore + TasteScore.SCORE_PLAYROOM_LIKE);
            }
            // 유저 정보 저장
            userInfo.setTasteInfo(tasteInfo);
            userInfoRepository.save(userInfo);
            // 유저 취향 분석 후 저장
            List<String> userTaste = TasteUtil.getTaste(tasteInfo);
            user.setTaste(userTaste);
            userRepository.save(user);

        } else {
            // 익셉션 발생
        }
    }

    @Transactional
    public void deletePlayroomLike(Long userSeq, Long playroomId) {

        PlayroomLikes existPlayroomLikes = playroomLikesRepository.findExist(userSeq, playroomId);
        if(existPlayroomLikes != null) {
            playroomLikesRepository.delete(existPlayroomLikes);

            // 한길 : playroom 에 좋아요 빠졌을 때 -1 하기
            Playroom playroom = playroomRepository.getById(playroomId);
            playroom.setLikesCnt(playroom.getLikesCnt()-1);
            playroomRepository.save(playroom);
        } else {
            // 익셉션 발생
        }
    }

    // 회원 입장시 발생
    @Transactional
    public void addGuest(Long userSeq, Playroom playroom) {
        List<Long> guests = playroom.getGuests();
        guests.add(userSeq);
        playroom.setGuests(guests);
        playroomRepository.save(playroom);

        // 취향분석
        User user = userRepository.findByUserSeq(userSeq);
        // 유저 취향 가져오기
        UserInfo userInfo = userInfoRepository.findOneByUserSeq(userSeq);
        ConcurrentHashMap<String, Integer> tasteInfo = userInfo.getTasteInfo();
        for (YoutubeVideo youtubeVideo : playroom.getVideos()) {
            // 카테고리에 따른 분류
            String category = CategoryList.CATEGORY_LIST.getOrDefault(youtubeVideo.getCategoryId(), "기타");
            // 취향 반영
            Integer tasteScore = tasteInfo.getOrDefault(category, 0);
            tasteInfo.put(category, tasteScore + TasteScore.SCORE_PLAYROOM_VISIT);
        }
        // 유저 정보 저장
        userInfo.setTasteInfo(tasteInfo);
        userInfoRepository.save(userInfo);
        // 유저 취향 분석 후 저장
        List<String> userTaste = TasteUtil.getTaste(tasteInfo);
        user.setTaste(userTaste);
        userRepository.save(user);
    }

    @Transactional
    public void deleteGuest(Long userSeq, Long playroomId) {
        Playroom playroom = playroomRepository.findById(playroomId).orElse(null);
        if (playroom != null) {
            List<Long> guests = playroom.getGuests();
            guests.remove(userSeq);
            playroom.setGuests(guests);
            playroomRepository.save(playroom);
        }
    }

    public List<Playroom> getMyPlayroom(Long userSeq, Pageable pageable) {
        return jpaQueryFactory
                .select(playroom)
                .from(playroom)
                .where(playroom.user.userSeq.eq(userSeq))
                .orderBy(playroom.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    // 해당 유저가 현재 시청하고있는 playroom list 가져오기 (최신 순 상위 10개)
    // json column contains 해결을 못하겠어요 ㅜㅜ 최적화 너무 필요!!! $$$
    public List<Playroom> getWatchingPlayroom(Long userSeq){
        List<Playroom> playrooms = playroomRepository.findAll();
        List<Playroom> result = new ArrayList<>();
        for(Playroom playroom : playrooms){
            if(playroom.getGuests().contains(userSeq)) {
                result.add(playroom);
                if(result.size() == 10) break;
            }
        }
        return result;
    }

}
