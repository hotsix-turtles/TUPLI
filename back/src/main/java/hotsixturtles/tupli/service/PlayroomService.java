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

    // ?????? querydsl
    private final JPAQueryFactory jpaQueryFactory;

    public List<Playroom> getPlayroomList(){

        // Paging ?????? ????????? ??????
        return playroomRepository.findAll();
    }

    // Home ?????? Playroom ??????
    public List<Playroom> getHomePlayrooms(Pageable pageable) {
        return playroomRepositoryCustom.listByHomePlayroom(pageable);
    }

    public Playroom getPlayroom(Long playroomId){

        // Paging ?????? ????????? ??????
        Playroom playroom = playroomRepository.findById(playroomId).orElse(null);
        return playroom;
//        return playroomRepository.findById(playroomId).orElse(null);
    }

    // ???????????? ????????? ?????? playroom
    public List<Playroom> getLikedPlayrooms(Long userSeq){
        return playroomRepository.findLikedPlayrooms(userSeq);
    }

    @Transactional
    public PlayroomDto addPlayroom(RequestPlayroomDto playroomDto, Long userSeq){

        Playroom playroom = new Playroom();

        // ???????????? : ??? ?????????(maker), ?????? ?????????(owner, ??????) << ?????? ???????????? ???????????? ??????
        User maker = userRepository.findByUserSeq(userSeq);
        playroom.setUser(maker);

        // ?????? ?????? ????????????
        UserInfo userInfo = userInfoRepository.findOneByUserSeq(userSeq);
        ConcurrentHashMap<String, Integer> tasteInfo = userInfo.getTasteInfo();

        playroom.setTitle(playroomDto.getTitle());
        playroom.setContent(playroomDto.getContent());
        playroom.setIsPublic(playroomDto.getIsPublic());
        playroom.setTags(playroomDto.getTags());
        playroom.setEndTime(playroomDto.getEndTime());
        playroom.setStartTime(playroomDto.getStartTime());
        playroom.setUserCountMax(playroomDto.getUserCountMax());
        playroom.setInviteIds(playroomDto.getInviteIds());

        Set<String> categorys = new HashSet<>();
        // ?????????????????? ????????? ???????????? ?????? + ID??? ??????
        ConcurrentHashMap<Long, List<Long>> playlists = new ConcurrentHashMap<>();
        ConcurrentHashMap<Integer, Integer> playroomInfo = new ConcurrentHashMap<Integer, Integer>();
        List<YoutubeVideo> youtubeVideoList = new ArrayList<>();
        for (Map.Entry<Long, List<String>> entry : playroomDto.getPlaylists().entrySet()) {
            // ?????????????????? ID??? ??????
//            playlists.add(entry.getKey());
            List<Long> playroomPlList = new ArrayList<>();


            String image = null;

            // ????????? ??????(?????????????????? ?????? ?????? ??? ?????? ?????? ????????? ?????? DB ?????? ??????)
            for (String videoUrl : entry.getValue()) {
                YoutubeVideo video = new YoutubeVideo();
                YoutubeVideo existVideo = youtubeVideoRepository.findFirstByVideoId(videoUrl);
                // ??? ?????? ???????????? ?????? (???????????????)
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

                // ???????????? ?????? ????????? ????????? ?????? ?????? ??????
                Integer categoryId = existVideo.getCategoryId();

                Integer count = playroomInfo.getOrDefault(categoryId, 0);
                playroomInfo.put(categoryId, count+1);

                // ??????????????? ?????? ??????
                String category = CategoryList.CATEGORY_LIST.getOrDefault(categoryId, "??????");
                categorys.add(category);

                // ?????? ??????
                Integer tasteScore = tasteInfo.getOrDefault(category, 0);
                tasteInfo.put(category, tasteScore + TasteScore.SCORE_PLAYROOM_MAKE);
            }
            playlists.put(entry.getKey(), playroomPlList);

        }
        playroom.setPlayroomInfo(playroomInfo);
        playroom.setPlaylists(playlists);

        // ????????? ?????? Stringify
        String categorysString = "";
        for (String category : categorys) {
            categorysString = categorysString + category + ", ";
        }

        playroom.setPlayroomCate(categorysString);
        Playroom nowPlayroom = playroomRepository.save(playroom);

        // ???????????? ?????? ?????? ????????? (?????? ??????)
        for (Long inviteId : playroomDto.getInviteIds()) {
            notificationService.notiPlayroomMake(userSeq, inviteId, nowPlayroom.getId());
        }

        // ???????????? ?????? ?????? ????????? (????????????, ???????????? ???????????????, ?????? ?????? ?????? ??????????????? ??? ??? ??????)
        if (false) {
            List<UserLikes> followers = userService.getFollowers(userSeq);
            for (UserLikes follower : followers) {
                notificationService.notiInvite(userSeq, follower.getFromUser().getUserSeq(), nowPlayroom.getId());
            }

        }

        // ?????? ?????? ??????
        userInfo.setTasteInfo(tasteInfo);
        userInfoRepository.save(userInfo);

        // ?????? ?????? ?????? ??? ??????
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

        User maker = userRepository.findByUserSeq(userSeq);
        youtubeVideoRepository.deleteVideos(playroomId);

        // ?????? ?????? ????????????
        UserInfo userInfo = userInfoRepository.findOneByUserSeq(userSeq);
        ConcurrentHashMap<String, Integer> tasteInfo = userInfo.getTasteInfo();

        Playroom playroom = playroomRepository.findById(playroomId).orElse(null);

        if(playroom == null || !Objects.equals(playroom.getUser().getUserSeq(), userSeq)){
            return null;
        }

        if(playroomDto.getTitle() != null){
            playroom.setTitle(playroomDto.getTitle());
        }
        if(playroomDto.getContent() != null){
            playroom.setContent(playroomDto.getContent());
        }
        if(playroomDto.getIsPublic() != null){
            playroom.setIsPublic(playroomDto.getIsPublic());
        }
        if(playroomDto.getTags() != null){
            playroom.setTags(playroomDto.getTags());
        }
        if(playroomDto.getStartTime() != null){
            playroom.setStartTime(playroomDto.getStartTime());
        }
        if(playroomDto.getEndTime() != null){
            playroom.setEndTime(playroomDto.getEndTime());
        }
        if(playroom.getUserCountMax() != null){
            playroom.setUserCountMax(playroomDto.getUserCountMax());
        }
        playroom.setInviteIds(playroomDto.getInviteIds());

        // ?????????????????? ????????? ???????????? ?????? + ID??? ??????
        ConcurrentHashMap<Long, List<Long>> playlists = new ConcurrentHashMap<>();
        ConcurrentHashMap<Integer, Integer> playroomInfo = new ConcurrentHashMap<Integer, Integer>();
        List<YoutubeVideo> youtubeVideoList = new ArrayList<>();
        for (Map.Entry<Long, List<String>> entry : playroomDto.getPlaylists().entrySet()) {
            // ?????????????????? ID??? ??????
//            playlists.add(entry.getKey());
            List<Long> playroomPlList = new ArrayList<>();


            String image = null;

            // ????????? ??????(?????????????????? ?????? ?????? ??? ?????? ?????? ????????? ?????? DB ?????? ??????) ????????? ????????? ??????!!!!! $$$
            for (String videoUrl : entry.getValue()) {
                YoutubeVideo video = new YoutubeVideo();
                YoutubeVideo existVideo = youtubeVideoRepository.findFirstByVideoId(videoUrl);
                // ??? ?????? ???????????? ?????? (???????????????)
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

                // ???????????? ?????? ????????? ????????? ?????? ?????? ??????
                Integer categoryId = existVideo.getCategoryId();
                Integer count = playroomInfo.getOrDefault(categoryId, 0);
                playroomInfo.put(categoryId, count+1);

                // ??????????????? ?????? ??????
                String category = CategoryList.CATEGORY_LIST.getOrDefault(categoryId, "??????");

                // ?????? ??????
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

        // ???????????? ??????
        Map<Integer, String> categoryList = new HashMap<>();
        List<Category> categories =  categoryRepository.findAll();
        for(Category category : categories){
            categoryList.put(category.getCategoryId().intValue(), category.getSort());
        }

        Set<String> categorys = new HashSet<>();

        // Video ??????
        ConcurrentHashMap<Integer, Integer> playroominfo = new ConcurrentHashMap<Integer, Integer>();
        String image = null;
        for (SimpleYoutubeVideoDto videoDto : playroomDto.getVideos()) {
            // ??? ?????? ???????????? ?????? (???????????????)
            if (image == null) {
                image = videoDto.getThumbnail();
                playroom.setImage(image);
            }

            // Playroominfo??? ?????? ??????
            Integer categoryId = videoDto.getCategoryId();
            Integer count = playroominfo.getOrDefault(categoryId, 0);
            playroominfo.put(categoryId, count+1);

            // ??????????????? ?????? ??????
            String category = categoryList.getOrDefault(categoryId, "??????");
            categorys.add(category);
        }

        // ????????? ?????? Stringify
        String categorysString = "";
        for (String category : categorys) {
            categorysString = categorysString + category + ", ";
        }

        playroom.setPlayroomCate(categorysString);
        Playroom nowPlayroom = playroomRepository.save(playroom);

        // ?????? ?????? ??????
        userInfo.setTasteInfo(tasteInfo);
        userInfoRepository.save(userInfo);

        // ?????? ?????? ?????? ??? ??????
        List<String> userTaste = TasteUtil.getTaste(tasteInfo);
        maker.setTaste(userTaste);
        userRepository.save(maker);

        return new PlayroomDto(nowPlayroom);
    }

    @Transactional
    public Playroom deletePlayroom(Long playroomId, Long userSeq){

        Playroom playroom = playroomRepository.findById(playroomId).orElse(null);

        //userSeq == -1L ?????? ?????????
        if(userSeq != -1L) {
            if (playroom == null || !Objects.equals(playroom.getUser().getUserSeq(), userSeq)) {
                return null;
            }
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

            // ?????? : playroom ??? ????????? ????????? ??? +1 ??????
            Playroom playroom = playroomRepository.getById(playroomId);
            playroom.setLikesCnt(playroom.getLikesCnt()+1);
            playroomRepository.save(playroom);

            // ????????????
            // ?????? ?????? ????????????
            UserInfo userInfo = userInfoRepository.findOneByUserSeq(userSeq);
            ConcurrentHashMap<String, Integer> tasteInfo = userInfo.getTasteInfo();
            for (YoutubeVideo youtubeVideo : playroom.getVideos()) {
                // ??????????????? ?????? ??????
                String category = CategoryList.CATEGORY_LIST.getOrDefault(youtubeVideo.getCategoryId(), "??????");
                // ?????? ??????
                Integer tasteScore = tasteInfo.getOrDefault(category, 0);
                tasteInfo.put(category, tasteScore + TasteScore.SCORE_PLAYROOM_LIKE);
            }
            // ?????? ?????? ??????
            userInfo.setTasteInfo(tasteInfo);
            userInfoRepository.save(userInfo);
            // ?????? ?????? ?????? ??? ??????
            List<String> userTaste = TasteUtil.getTaste(tasteInfo);
            user.setTaste(userTaste);
            userRepository.save(user);

        } else {
            // ????????? ??????
        }
    }

    @Transactional
    public void deletePlayroomLike(Long userSeq, Long playroomId) {

        PlayroomLikes existPlayroomLikes = playroomLikesRepository.findExist(userSeq, playroomId);
        if(existPlayroomLikes != null) {
            playroomLikesRepository.delete(existPlayroomLikes);

            // ?????? : playroom ??? ????????? ????????? ??? -1 ??????
            Playroom playroom = playroomRepository.getById(playroomId);
            playroom.setLikesCnt(playroom.getLikesCnt()-1);
            playroomRepository.save(playroom);
        } else {
            // ????????? ??????
        }
    }

    // ?????? ????????? ??????
    @Transactional
    public void addGuest(Long userSeq, Playroom playroom) {
        List<Long> guests = playroom.getGuests();
        guests.add(userSeq);
        playroom.setGuests(guests);
        playroomRepository.save(playroom);

        // ????????????
        User user = userRepository.findByUserSeq(userSeq);
        // ?????? ?????? ????????????
        UserInfo userInfo = userInfoRepository.findOneByUserSeq(userSeq);
        ConcurrentHashMap<String, Integer> tasteInfo = userInfo.getTasteInfo();
        for (YoutubeVideo youtubeVideo : playroom.getVideos()) {
            // ??????????????? ?????? ??????
            String category = CategoryList.CATEGORY_LIST.getOrDefault(youtubeVideo.getCategoryId(), "??????");
            // ?????? ??????
            Integer tasteScore = tasteInfo.getOrDefault(category, 0);
            tasteInfo.put(category, tasteScore + TasteScore.SCORE_PLAYROOM_VISIT);
        }
        // ?????? ?????? ??????
        userInfo.setTasteInfo(tasteInfo);
        userInfoRepository.save(userInfo);
        // ?????? ?????? ?????? ??? ??????
        List<String> userTaste = TasteUtil.getTaste(tasteInfo);
        user.setTaste(userTaste);
        userRepository.save(user);
    }

    @Transactional
    public void deleteGuest(Long userSeq, Long playroomId) {
        Playroom playroom = playroomRepository.findById(playroomId).orElse(null);
        if (playroom != null) {
            List<Long> guests = playroom.getGuests();
//            if (guests.contains(userSeq)) {
            guests.remove(userSeq);
//            }
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

    // ?????? ????????? ?????? ?????????????????? playroom list ???????????? (?????? ??? ?????? 10???)
    // json column contains ????????? ??????????????? ?????? ????????? ?????? ??????!!! $$$
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
