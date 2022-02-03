package hotsixturtles.tupli.repository;

import hotsixturtles.tupli.dto.params.BoardSearchCondition;
import hotsixturtles.tupli.entity.Board;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardRepositoryCustom {
    List<Board> searchByPageSimpleBoard(BoardSearchCondition boardSearchCondition, Pageable pageable);
}
