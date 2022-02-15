package hotsixturtles.tupli.scheduler;

import hotsixturtles.tupli.entity.meta.UserInfo;
import hotsixturtles.tupli.repository.SearchHistoryRepository;
import hotsixturtles.tupli.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 매일 0시(서버시간 기준) 출석체크 가능 상태로 변함
 */
@Component
@RequiredArgsConstructor
public class DailyCheckScheduler {

    private final UserInfoRepository userInfoRepository;

    private final SearchHistoryRepository searchHistoryRepository;

    @Scheduled(cron="0 0 0 * * *")
    @Transactional
    public void DailyCheck(){
//        List<UserInfo> userInfoList = userInfoRepository.findAll();
//        for(UserInfo nowUserInfo : userInfoList){
//            nowUserInfo.setDailyLoginYN("N");
//            userInfoRepository.save(nowUserInfo);
//        }
        userInfoRepository.updateDailyCheck();
    }

    // 매 30분 search Keyword 체크 후 값 바꿔줌
    @Scheduled(cron="0 */30 * * * *")
    @Transactional
    public void realTimeKeywordCheck() {
        searchHistoryRepository.updateScore();
    }

    // 매 시간 search 안된 검색어에 부가 값 증가
    @Scheduled(cron="0 0 * * * *")
    @Transactional
    public void realTimeSearchPlus(){
//        List<SearchHistory> searchHistoryList = searchHistoryRepository.findAll();
//        for(SearchHistory searchHistory: searchHistoryList){
//            searchHistory.setNoSearch(searchHistory.getNoSearch()+ 1);
//            searchHistoryRepository.save(searchHistory);
//        }
        searchHistoryRepository.updateNoSearch();
    }

    // 하루에 한번 씩 score 0인 애들 정리 (search에 10개 넘을때만)
    @Scheduled(cron="5 0 0 * * *")
    @Transactional
    public void updateRealTimeKeyWord(){
        int allLen = searchHistoryRepository.getCountAll();
        int zeroLen = searchHistoryRepository.getCountLtZero();

        if(allLen - zeroLen >= 10){
            searchHistoryRepository.deleteByScoreLessThan(0);
        }

//        List<SearchHistory> searchHistoryList = searchHistoryRepository.findAll();
//        for(SearchHistory searchHistory: searchHistoryList){
//            searchHistory.setScore(searchHistory.getScore() + 10);
//            searchHistory.setNoSearch(searchHistory.getNoSearch()+ 1);
//            searchHistoryRepository.save(searchHistory);
//        }

        searchHistoryRepository.updateDailyKeywords();

    }


}
