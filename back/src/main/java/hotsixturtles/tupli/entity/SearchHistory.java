package hotsixturtles.tupli.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "search_history")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SearchHistory {

    @Id
    @GeneratedValue
    private Long id;

    private String type; // (예시 플레이리스트)

    private String keyword; // (검색어)

    private Integer score; // : 처음 0 (검색 한 번 될때마다 +1)

}
