package hotsixturtles.tupli.dto.simple;

import hotsixturtles.tupli.entity.SearchHistory;
import lombok.Data;

@Data
public class SimpleSearchHistoryDto {

    private Long id;

    private String type; // (예시 플레이리스트)

    private String keyword; // (검색어)

    private Integer score; // : 처음 0 (검색 한 번 될때마다 +1)

    public SimpleSearchHistoryDto(SearchHistory searchHistory){
        this.id = searchHistory.getId();
        this.type = searchHistory.getType();
        this.keyword = searchHistory.getKeyword();
        this.score = searchHistory.getScore();
    }

}
