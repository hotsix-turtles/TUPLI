package hotsixturtles.tupli.repository;

import hotsixturtles.tupli.entity.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
    List<SearchHistory> findTop10ByOrderByScoreDescIdDesc();
    List<SearchHistory> findByKeyword(String keyword);


    @Query("select count(s) from SearchHistory s")
    Integer getCountAll();

    @Query("select count(s) from SearchHistory s where s.score = 0")
    Integer getCountZero();

    void deleteByScoreEquals(Integer score);

}
