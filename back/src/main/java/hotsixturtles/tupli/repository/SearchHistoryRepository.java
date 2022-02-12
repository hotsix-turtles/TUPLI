package hotsixturtles.tupli.repository;

import hotsixturtles.tupli.entity.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
    List<SearchHistory> findTop10ByOrderByScoreDesc();
    List<SearchHistory> findByKeyword(String keyword);
}
