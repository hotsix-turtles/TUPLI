package hotsixturtles.tupli.repository;

import hotsixturtles.tupli.entity.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
    List<SearchHistory> findTop10ByOrderByScoreDescIdDesc();
    List<SearchHistory> findByKeyword(String keyword);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update SearchHistory s set s.score = s.score /12 * s.noSearch - 1 where s.score > 0")
    void updateScore();

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update SearchHistory s set s.noSearch = s.noSearch + 1")
    void updateNoSearch();

    @Query("select count(s) from SearchHistory s")
    Integer getCountAll();

    @Query("select count(s) from SearchHistory s where s.score < 0")
    Integer getCountLtZero();

    void deleteByScoreLessThan(Integer score);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update SearchHistory s set s.score = s.score + 10")
    void updateDailyKeywords();


}
