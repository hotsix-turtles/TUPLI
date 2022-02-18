package hotsixturtles.tupli.service;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hotsixturtles.tupli.dto.PlaylistDto;
import hotsixturtles.tupli.dto.param.SimpleCondition;
import hotsixturtles.tupli.dto.params.PlaylistSearchCondition;
import hotsixturtles.tupli.dto.request.PlaylistRequest;
import hotsixturtles.tupli.dto.simple.SimpleHomeInfoDto;
import hotsixturtles.tupli.dto.simple.SimplePlaylistDto;
import hotsixturtles.tupli.dto.simple.SimpleYoutubeVideoDto;
import hotsixturtles.tupli.entity.HomeInfo;
import hotsixturtles.tupli.entity.Playlist;
import hotsixturtles.tupli.entity.User;
import hotsixturtles.tupli.entity.likes.PlaylistLikes;
import hotsixturtles.tupli.entity.meta.UserInfo;
import hotsixturtles.tupli.entity.youtube.YoutubeVideo;
import hotsixturtles.tupli.repository.*;
import hotsixturtles.tupli.repository.likes.PlaylistLikesRepository;
import hotsixturtles.tupli.service.list.CategoryList;
import hotsixturtles.tupli.service.list.TasteScore;
import hotsixturtles.tupli.utils.TasteUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static hotsixturtles.tupli.entity.QPlaylist.*;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlaylistService {

    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final PlaylistRepository playlistRepository;
    private final PlaylistLikesRepository playlistLikesRepository;
    private final YoutubeVideoRepository youtubeVideoRepository;
    private final PlaylistRepositoryCustom playlistRepositoryCustom;
    private final HomeInfoRepository homeInfoRepository;

    // 심플 querydsl
    private final JPAQueryFactory jpaQueryFactory;

    // 전체 DB 플레이리스트 다 가져오기
    public List<Playlist> getPlaylistList() {
        return playlistRepository.findAll();
    }

    // 단일 Playlist 추가
    @Transactional
    public Playlist addPlaylist(Long userSeq, PlaylistRequest playlistRequest) {
        // 기본 정보
        Playlist playlist = new Playlist();
        playlist.setTitle(playlistRequest.getTitle());
        playlist.setContent(playlistRequest.getContent());
        playlist.setTags(playlistRequest.getTags());
        playlist.setIsPublic(playlistRequest.getIsPublic());
        
        // 연결
        User user = userRepository.findByUserSeq(userSeq);
        playlist.setUser(user);

        // 유저 취향 가져오기
        UserInfo userInfo = userInfoRepository.findOneByUserSeq(userSeq);
        ConcurrentHashMap<String, Integer> tasteInfo = userInfo.getTasteInfo();

        // 카테고리 정보 담을 Set
        Set<String> categorys = new HashSet<>();

        // Video 정보
        ConcurrentHashMap<Integer, Integer> playlistInfo = new ConcurrentHashMap<Integer, Integer>();
        String image = null;
        for (SimpleYoutubeVideoDto videoDto : playlistRequest.getVideos()) {
            // 첫 영상 이미지만 저장 (미리보기용)
            if (image == null) {
                image = videoDto.getThumbnail();
                playlist.setImage(image);
            }

            // 기존에 저장, 좋아요 해놓은것과 상관없이 별도로 제작
            YoutubeVideo video = new YoutubeVideo();
            video.newVideo(videoDto);
            video.setPlaylist(playlist);  // 연결
            youtubeVideoRepository.save(video);

            // Playlistinfo에 따라 갈림
            Integer categoryId = videoDto.getCategoryId();
            Integer count = playlistInfo.getOrDefault(categoryId, 0);
            playlistInfo.put(categoryId, count+1);

            // 카테고리에 따른 분류 (구현 두 종류)
            String category = CategoryList.CATEGORY_LIST.getOrDefault(categoryId, "기타");
            categorys.add(category);

            // 취향 반영
            Integer tasteScore = tasteInfo.getOrDefault(category, 0);
            tasteInfo.put(category, tasteScore + TasteScore.SCORE_PLAYLIST_MAKE);
        }

        // 검색을 위한 Stringify
        String categorysString = "";
        for (String category : categorys) {
            categorysString = categorysString + category + ", ";
        }
        playlist.setPlaylistCate(categorysString);
        playlist.setPlaylistInfo(playlistInfo);

        // 유저 정보 저장
        userInfo.setTasteInfo(tasteInfo);
        userInfoRepository.save(userInfo);

        // 유저 취향 분석 후 저장
        List<String> userTaste = getTaste(tasteInfo);
        user.setTaste(userTaste);
        userRepository.save(user);

        // 최종 저장
        Playlist nowPlaylist = playlistRepository.save(playlist);

        HomeInfo homeInfo = new HomeInfo();
        homeInfo.setType("playlist");
        homeInfo.setInfoId(nowPlaylist.getId());
        homeInfo.setUserSeq(userSeq);
        homeInfoRepository.save(homeInfo);

        return playlist;
    }

    private List<String> getTaste(ConcurrentHashMap<String, Integer> tasteInfo) {
        List<String> userTaste = new ArrayList<>();
        // 유저 정보 분석, MAP을 Value DESC로 나열. 최대 넷.
        Map<String, Integer> topFour =
                tasteInfo.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .limit(5)
                        .collect(Collectors.toMap(
                                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        for (String category : topFour.keySet()) {
            userTaste.add(category);
        }
        return userTaste;
    }

    // 단일 Playlist id로 검색
    public Playlist getPlaylist(Long playlistId) {
        return playlistRepository.findById(playlistId).orElse(null);
    }

    public List<SimplePlaylistDto> getRecommendPlaylist(List<Long> recommendPlaylist){
        List<SimplePlaylistDto> recommendPlaylists = new ArrayList<>();
        if (recommendPlaylist == null) return null;
        for(Long recommendPlaylistId : recommendPlaylist){
            Playlist recommendPlaylistPL = playlistRepository.findById(recommendPlaylistId).orElse(null);
            if(recommendPlaylistPL == null) continue;
            recommendPlaylists.add(new SimplePlaylistDto(recommendPlaylistPL));
        }
        return recommendPlaylists;
    }

    public List<Playlist> getHomePlaylists(Pageable pageable){
        return playlistRepositoryCustom.listByHomePlaylist(pageable);
    }
    // 단일 Playlist id로 update
    @Transactional
    public void updatePlaylist(Long playlistId, PlaylistRequest playlistRequest) {
        Playlist playlistUpdate = playlistRepository.findById(playlistId).orElse(null);

        if (playlistUpdate!= null) {
            // 기본 정보
            playlistUpdate.setTitle(playlistRequest.getTitle());
            playlistUpdate.setContent(playlistRequest.getContent());
            playlistUpdate.setTags(playlistRequest.getTags());
            playlistUpdate.setIsPublic(playlistRequest.getIsPublic());

            // 연결 (제작자가 바뀌지는 않을테니 pass)

            // Video 기존 정보 삭제 & 초기화 (이유 : 1. ID, 2. 연결 부분 해제시의 오류)
            for (YoutubeVideo youtubeVideo : playlistUpdate.getYoutubeVideos()) {
                youtubeVideoRepository.delete(youtubeVideo);
            }

            // 카테고리 정보 담을 Set
            Set<String> categorys = new HashSet<>();

            // Video 정보
            ConcurrentHashMap<Integer, Integer> playlistInfo = new ConcurrentHashMap<Integer, Integer>();
            String image = null;
            for (SimpleYoutubeVideoDto videoDto : playlistRequest.getVideos()) {
                // 첫 영상 이미지만 저장 (미리보기용)
                if (image == null) {
                    image = videoDto.getThumbnail();
                    playlistUpdate.setImage(image);
                }

                // 기존에 저장, 좋아요 해놓은것과 상관없이 별도로 제작
                YoutubeVideo video = new YoutubeVideo();
                video.newVideo(videoDto);
                video.setPlaylist(playlistUpdate);  // 연결
                youtubeVideoRepository.save(video);

                // Playlistinfo에 따라 갈림
                Integer categoryId = videoDto.getCategoryId();
                Integer count = playlistInfo.getOrDefault(categoryId, 0);
                playlistInfo.put(categoryId, count+1);

                // 카테고리에 따른 분류 (구현 두 종류)
                String category = CategoryList.CATEGORY_LIST.getOrDefault(categoryId, "기타");
//            String category = categoryList.getOrDefault(categoryId, "기타");
                categorys.add(category);
            }

            // 검색을 위한 Stringify
            String categorysString = "";
            for (String category : categorys) {
                categorysString = categorysString + category + ", ";
            }
            playlistUpdate.setPlaylistCate(categorysString);
            playlistUpdate.setPlaylistInfo(playlistInfo);

            playlistRepository.save(playlistUpdate);
        } else {
            // 원래는 error throw 라도 해줘야
        }
    }

    // 단일 Playlist id로 delete
    @Transactional
    public void deletePlaylist(Long playlistId) {

        playlistRepository.deleteById(playlistId);
        homeInfoRepository.deleteByInfoId(playlistId);
    }

    // 사용자가 해당 플레이리스트 좋아요 했는지.
    public PlaylistLikes getPlaylistLike(Long userSeq, Long id) {
        return playlistLikesRepository.findExist(userSeq, id);
    }

    // 사용자가 해당 플레이리스트에 좋아요
    @Transactional
    public void addPlaylistLike(Long userSeq, Long id) {
        PlaylistLikes playlistLikes = playlistLikesRepository.findExist(userSeq, id);
        if(playlistLikes == null) {
            User user = userRepository.findByUserSeq(userSeq);

            playlistLikes = new PlaylistLikes();
            playlistLikes.setUser(user);
            playlistLikes.setPlaylist(playlistRepository.findById(id).orElse(null));
            playlistLikesRepository.save(playlistLikes);

            // 한길 : playlist 에 좋아요 넣으면 +1 하기
            Playlist playlist = playlistRepository.getById(id);
            playlist.setLikesCnt(playlist.getLikesCnt()+1);
            playlistRepository.save(playlist);

            // 취향분석
            // 유저 취향 가져오기
            UserInfo userInfo = userInfoRepository.findOneByUserSeq(userSeq);
            ConcurrentHashMap<String, Integer> tasteInfo = userInfo.getTasteInfo();
            for (YoutubeVideo youtubeVideo : playlist.getYoutubeVideos()) {
                // 카테고리에 따른 분류
                String category = CategoryList.CATEGORY_LIST.getOrDefault(youtubeVideo.getCategoryId(), "기타");
                // 취향 반영
                Integer tasteScore = tasteInfo.getOrDefault(category, 0);
                tasteInfo.put(category, tasteScore + TasteScore.SCORE_PLAYLIST_LIKE);
            }
            // 유저 정보 저장
            userInfo.setTasteInfo(tasteInfo);
            userInfoRepository.save(userInfo);
            // 유저 취향 분석 후 저장
            List<String> userTaste = TasteUtil.getTaste(tasteInfo);
            user.setTaste(userTaste);
            userRepository.save(user);

        } else {
            // exception 발생
        }
    }

    // 사용자가 해당 플레이리스트에 좋아요 취소
    @Transactional
    public void deletePlaylistLike(Long userSeq, Long id) {
        PlaylistLikes playlistLikes = playlistLikesRepository.findExist(userSeq, id);
        if(playlistLikes != null) {
            playlistLikesRepository.delete(playlistLikes);

            // 한길 : playlist 에 좋아요 취소하면 -1 하기
            Playlist playlist = playlistRepository.getById(id);
            playlist.setLikesCnt(playlist.getLikesCnt()-1);
            playlistRepository.save(playlist);

        } else {
            // exception 발생
        }
    }

    // 사용자가 좋아하는 플레이리스트 리스트 가져오기
    public List<Playlist> getLikedPlaylists(Long userSeq) {
        return playlistRepository.findLikedPlaylists(userSeq);
    }

    // Querydsl 버전 (굳이 querydsl로 작성할 이유 하나도 없는 쿼리)
    public List<Playlist> searchPlaylistSimple(PlaylistSearchCondition condition, String order, Pageable pageable) {
        // QueryProjection(select에 Q타입 담기) 쓰고 싶으면 다음 기회에!
        // QPlaylist.playlist를 -> playlist로 static import 했습니다.(강민구)
        // NumberExpression 사용금지!
        // 이유 반환이 Tuple인데 변환하려면 전용 생성자 필요해서 DTO가 더러워짐, 다른 방법 아시는 분 말씀해주심 감사!
                
        // 기본값 : 관련도 순 (이름순, 작성자, 내용 순 + 나중에 이름 정해졌을때 조건 변경 필요 )
        if(order.equals("relevance")) {
            JPAQuery<Playlist> query = jpaQueryFactory
                    .select(playlist)
                    .distinct()
                    .from(playlist)
                    .where(titleContains(condition.getKeyword())
                            .or(tagContains(condition.getKeyword()))
                            .or(usernameContains(condition.getKeyword()))
                            .or(descriptionContains(condition.getKeyword())))
                    .orderBy(playlist.title.asc()
                            , playlist.user.username.asc()
                            , playlist.tags.asc()
                            , playlist.content.asc()
                    )
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize());

            List<Playlist> result = query.fetch();
            return result;
        }
        else if(order.equals("date")){
            JPAQuery<Playlist> query = jpaQueryFactory
                    .select(playlist)
                    .distinct()
                    .from(playlist)
                    .where(titleContains(condition.getKeyword())
                            .or(tagContains(condition.getKeyword()))
                            .or(usernameContains(condition.getKeyword()))
                            .or(descriptionContains(condition.getKeyword())))
                    .orderBy(playlist.createdAt.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize());

            List<Playlist> result = query.fetch();
            return result;
        }
        else{
            JPAQuery<Playlist> query = jpaQueryFactory
                    .select(playlist)
                    .distinct()
                    .from(playlist)
                    .where(titleContains(condition.getKeyword())
                            .or(tagContains(condition.getKeyword()))
                            .or(usernameContains(condition.getKeyword()))
                            .or(descriptionContains(condition.getKeyword())))
                    .orderBy(playlist.likesCnt.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize());

            List<Playlist> result = query.fetch();
            return result;
        }
    }

    //인피니티 스크롤 대비 연습용 (현재 미사용)
    public List<Playlist> searchPlaylistPage(SimpleCondition condition, Pageable pageable) {

        // 기본값 : 관련도 순 (이름순, 작성자, 내용 순 + 나중에 이름 정해졌을때 조건 변경 필요 )
        if (condition.getType() == null | condition.getType() == "관련도순타입명설정필요" ) {
            JPAQuery<Playlist> query = jpaQueryFactory
                    .selectFrom(playlist)
                    .where(titleContains(condition.getKeyword())
                            .or(usernameContains(condition.getKeyword()))
                            .or(descriptionContains(condition.getKeyword())))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize());

            //이거 끝나면 query의 sort 완료됨
            for (Sort.Order o : pageable.getSort()) {
                PathBuilder pathBuilder = new PathBuilder(playlist.getType(), playlist.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            }

            List<Playlist> result = query.fetch();
            return result;
        }
        // 다른 타입 : 최신순
        else {
            return jpaQueryFactory
                    .selectFrom(playlist)
                    .where(titleContains(condition.getKeyword())
                            .or(usernameContains(condition.getKeyword()))
                            .or(descriptionContains(condition.getKeyword())))
                    .orderBy(playlist.id.desc())
                    .fetch();
        }
    }

    // 빌더와 다르게 재사용 가능해서 좋아함
    private BooleanExpression titleContains(String keyword) {
        return hasText(keyword) ? playlist.title.contains(keyword) : null;
    }
    private BooleanExpression usernameContains(String keyword) {
        return hasText(keyword) ? playlist.user.username.contains(keyword) : null;
    }
    private BooleanExpression descriptionContains(String keyword) {
        return hasText(keyword) ? playlist.content.contains(keyword) : null;
    }
    private BooleanExpression tagContains(String keyword) {
        return hasText(keyword) ? playlist.tags.contains(keyword) : null;
    }

    public List<Playlist> getMyPlaylist(Long userSeq, Pageable pageable) {
        return jpaQueryFactory
                .select(playlist)
                .from(playlist)
                .where(playlist.user.userSeq.eq(userSeq))
                .orderBy(playlist.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
