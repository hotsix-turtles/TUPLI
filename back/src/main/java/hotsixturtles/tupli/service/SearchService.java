package hotsixturtles.tupli.service;

import hotsixturtles.tupli.dto.params.*;
import hotsixturtles.tupli.entity.*;
import hotsixturtles.tupli.entity.youtube.YoutubeVideo;
import hotsixturtles.tupli.repository.*;
import hotsixturtles.tupli.dto.params.UserSearchCondition;
import hotsixturtles.tupli.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final UserRepositoryImpl searchUserRepository;

    private final PlayroomRepositoryImpl searchPlayroomRepository;

    private final BoardRepositoryImpl boardRepository;

    private final YoutubeVideoRepositoryImpl searchYoutubeVideoRepository;

    private final SearchHistoryRepository searchHistoryRepository;

    private final PlaylistRepositoryImpl searchPlaylistRepository;

    private final CategoryRepository categoryRepository;

    public List<User> searchUser(UserSearchCondition userSearchCondition, String order, Pageable pageable){
        return searchUserRepository.searchByPageSimpleUser(userSearchCondition, order, pageable);
    }

    public List<Playroom> searchPlayroom(PlayroomSearchCondition playroomSearchCondition, String order, Pageable pageable){
        return searchPlayroomRepository.searchByPageSimplePlayroom(playroomSearchCondition, order, pageable);
    }

    public List<Board> searchBoard(BoardSearchCondition boardSearchCondition, Pageable pageable){
        return boardRepository.searchByPageSimpleBoard(boardSearchCondition, pageable);
    }

    public List<YoutubeVideo> searchVideo(VideoSearchCondition videoSearchCondition, Pageable pageable){
        return searchYoutubeVideoRepository.searchByPageSimpleVideo(videoSearchCondition, pageable);
    }
    public List<Playlist> searchPlaylist(PlaylistSearchCondition playlistSearchCondition, Pageable pageable){
        return searchPlaylistRepository.searchByPageSimplePlaylist(playlistSearchCondition, pageable);
    }

    public List<SearchHistory> searchRankList(){
        return searchHistoryRepository.findTop10ByOrderByScoreDescIdDesc();
    }

    @Transactional
    public void addScoreNum(SearchHistory searchHistory){
        List<SearchHistory> searchHistoryList = searchHistoryRepository.findByKeyword(searchHistory.getKeyword().trim());
        if(searchHistoryList != null && searchHistoryList.size() != 0) {
            for (SearchHistory findHistory : searchHistoryList) {
                if (findHistory.getKeyword().trim().equals(searchHistory.getKeyword().trim())
                        && findHistory.getType().equals(searchHistory.getType())) {
                    findHistory.setScore(findHistory.getScore() + 10);
                    findHistory.setNoSearch(0);
                    searchHistoryRepository.save(findHistory);
                    return;
                }
            }
        }

        searchHistory.setScore(10);
        searchHistoryRepository.save(searchHistory);

    }

    // ???????????? ?????? ?????????????????? (????????? ??????)
    public List<Playlist> categoryPlaylist(String category, Pageable pageable) {
        if (category.equals("????????????")) {
            return searchPlaylistRepository.listByHomePlaylist(pageable);
        }

        return searchPlaylistRepository.categorizedByPageSimplePlaylist(category, pageable);
    }

    // ???????????? ?????? ???????????? (????????? ??????)
    // ????????? ???????????? ???????????? ????????????.... ?????? ??????????????????.. ( Tag ????????? )
    public List<Playroom> categoryPlayroom(String category, Pageable pageable) {
        if (category.equals("????????????")) {
            return searchPlayroomRepository.listByHomePlayroom(pageable);
        }
        return searchPlayroomRepository.categorizedByPageSimplePlayroom(category, pageable);
    }

    // ???????????? ?????? ????????? (????????? ??????)
    public List<YoutubeVideo> categoryVideos(String category, Pageable pageable) {
        if (category.equals("????????????")) {
            return searchYoutubeVideoRepository.searchNoConditionByPageVideo(pageable);
        }
        List<Category> categorylist =  categoryRepository.findAllBySort(category);
        List<Long> categoryIdList = new ArrayList<>();
        for(Category nowCategory : categorylist){
            categoryIdList.add(nowCategory.getCategoryId());
        }

        // ????????? ????????? ??? ?????? ?????? ?????? => ?????? for????????? ????????? ?????? ???????????? ??????
        List<YoutubeVideo> videoList = searchYoutubeVideoRepository.findDistinctByVideoId(pageable);

        List<YoutubeVideo> result = new ArrayList<>();

        for(YoutubeVideo video : videoList){
            if(categoryIdList.contains(Long.valueOf(video.getCategoryId()))){
                result.add(video);
            }
        }

        return result;
    }

}
