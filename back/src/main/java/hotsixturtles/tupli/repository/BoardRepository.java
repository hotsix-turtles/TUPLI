package hotsixturtles.tupli.repository;

import hotsixturtles.tupli.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("select b from Board b join BoardLikes bl on b.id = bl.board.id " +
            "where bl.user.userSeq = :userSeq")
    List<Board> findLikedBoards(@Param("userSeq") Long userSeq);
}
