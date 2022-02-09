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

    private final PlayroomRepository playroomRepository;

    private final BoardRepositoryImpl boardRepository;

    private final YoutubeVideoRepositoryImpl searchYoutubeVideoRepository;

    private final SearchHistoryRepository searchHistoryRepository;

    private final PlaylistRepositoryImpl searchPlaylistRepository;

    private final PlaylistRepository playlistRepository;

    private final TagRepository tagRepository;

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
        return searchYoutubeVideoRepository.searchByPageSimpleVideo(videoSearchCondition, pageable);
    }
    public List<Playlist> searchPlaylist(PlaylistSearchCondition playlistSearchCondition, Pageable pageable){
        return searchPlaylistRepository.searchByPageSimplePlaylist(playlistSearchCondition, pageable);
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

    // 카테고리 분류 플레이리스트 (나중에 이동)
    public List<Playlist> categoryPlaylist(String category, Pageable pageable) {
        if (category == "지금핫한" || category == "") {
            // 현재 비교방식 애매함
            return searchPlaylistRepository.listByHomePlaylist(pageable);
        }

        List<Tag> taglist =  tagRepository.findAllBySort(category);
        List<Long> tagIdList = new ArrayList<>();
        for(Tag tag : taglist){
            tagIdList.add(tag.getTagId());
        }

        // 나중에 고도화 때 쿼리 변경 필요 => 밑에 for문이랑 합쳐서 쿼리 구성해야 할듯
        Page<Playlist> playlists = playlistRepository.findAll(pageable);
        List<Playlist> plList = playlists.getContent();

        List<Playlist> result = new ArrayList<>();

        for(Playlist playlist : plList){
            for(ConcurrentHashMap.Entry<Integer, Integer> entry : playlist.getPlaylistInfo().entrySet()) {
                Long key = Long.valueOf(entry.getKey());
                if(tagIdList.contains(key)){
                    result.add(playlist);
                    break;
                }
            }
        }

        return result;
    }

    // 카테고리 분류 플레이룸 (나중에 이동)
    // 태그만 들어있는 테이블이 필요할듯.... 대충 만들었습니다.. ( Tag 엔티티 )
    public List<Playroom> categoryPlayroom(String category, Pageable pageable) {
        if (category == "지금핫한" || category == "") {
            return searchPlayroomRepository.listByHomePlayroom(pageable);
        }
        List<Tag> taglist =  tagRepository.findAllBySort(category);
        List<Long> tagIdList = new ArrayList<>();
        for(Tag tag : taglist){
            tagIdList.add(tag.getTagId());
        }

        // 나중에 고도화 때 쿼리 변경 필요 => 밑에 for문이랑 합쳐서 쿼리 구성해야 할듯
        Page<Playroom> playrooms = playroomRepository.findAll(pageable);
        List<Playroom> playroomList = playrooms.getContent();

        List<Playroom> result = new ArrayList<>();

        for(Playroom playroom : playroomList){
            for(ConcurrentHashMap.Entry<Integer, Integer> entry : playroom.getPlayroomInfo().entrySet()) {
                Long key = Long.valueOf(entry.getKey());
                if(tagIdList.contains(key)){
                    result.add(playroom);
                    break;
                }
            }
        }

        return result;
    }

    // 카테고리 분류 비디오 (나중에 이동)
    public List<YoutubeVideo> categoryVideos(String category, Pageable pageable) {
        if (category == "지금핫한" || category == "") {
            return searchYoutubeVideoRepository.searchNoConditionByPageVideo(pageable);
        }
        List<Tag> taglist =  tagRepository.findAllBySort(category);
        List<Long> tagIdList = new ArrayList<>();
        for(Tag tag : taglist){
            tagIdList.add(tag.getTagId());
        }

        // 나중에 고도화 때 쿼리 변경 필요 => 밑에 for문이랑 합쳐서 쿼리 구성해야 할듯
        List<YoutubeVideo> videoList = searchYoutubeVideoRepository.findDistinctByVideoId(pageable);
//        List<YoutubeVideo> videoList = videos.getContent();

        List<YoutubeVideo> result = new ArrayList<>();

        for(YoutubeVideo video : videoList){
            if(tagIdList.contains(Long.valueOf(video.getCategoryId()))){
                result.add(video);
            }
        }

        return result;
    }

}
