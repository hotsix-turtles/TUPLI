package hotsixturtles.tupli.service;

import hotsixturtles.tupli.dto.params.*;
import hotsixturtles.tupli.entity.*;
import hotsixturtles.tupli.entity.youtube.YoutubeVideo;
import hotsixturtles.tupli.repository.*;
import hotsixturtles.tupli.dto.params.UserSearchCondition;
import hotsixturtles.tupli.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final UserRepositoryImpl searchUserRepository;

    private final PlayroomRepositoryImpl searchPlayroomRepository;

    private final BoardRepositoryImpl boardRepository;

    private final YoutubeVideoRepositoryImpl youtubeVideoRepository;

    private final SearchHistoryRepository searchHistoryRepository;

    private final PlaylistRepositoryImpl playlistRepository;

    public List<User> searchUser(UserSearchCondition userSearchCondition, Pageable pageable){
        return searchUserRepository.searchByPageSimpleUser(userSearchCondition, pageable);
    }

    public List<Playroom> searchPlayroom(PlayroomSearchCondition playroomSearchCondition, Pageable pageable){
        return searchPlayroomRepository.searchByPageSimplePlayroom(playroomSearchCondition, pageable);
    }

    public List<Board> searchBoard(BoardSearchCondition boardSearchCondition, Pageable pageable){
        return boardRepository.searchByPageSimpleBoard(boardSearchCondition, pageable);
    }

    public List<YoutubeVideo> searchVideo(VideoSearchCondition videoSearchCondition, Pageable pageable){
        return youtubeVideoRepository.searchByPageSimpleVideo(videoSearchCondition, pageable);
    }
    public List<Playlist> searchPlaylist(PlaylistSearchCondition playlistSearchCondition, Pageable pageable){
        return playlistRepository.searchByPageSimplePlaylist(playlistSearchCondition, pageable);
    }

    public List<SearchHistory> searchRankList(){
        return searchHistoryRepository.findTop10ByOrderByScoreDesc();
    }

    // $$ 고도화 시 스케줄러 이용 시간 비교해서 오래된건 초기화 시켜줘야할듯
    @Transactional
    public void addScoreNum(SearchHistory searchHistory){
        List<SearchHistory> searchHistoryList = searchHistoryRepository.findByKeyword(searchHistory.getKeyword().trim());

        if(searchHistoryList.size() != 0){
            for(SearchHistory findHistory: searchHistoryList){
                if(findHistory.getKeyword().trim().equals(searchHistory.getKeyword().trim())
                        && findHistory.getType().equals(searchHistory.getType())){
                    findHistory.setScore(findHistory.getScore() + 1);
                    return;
                }
            }
        }

        searchHistory.setScore(1);
        searchHistoryRepository.save(searchHistory);
    }
}
