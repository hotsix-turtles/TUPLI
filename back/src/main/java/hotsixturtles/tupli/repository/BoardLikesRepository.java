package hotsixturtles.tupli.repository;

import hotsixturtles.tupli.entity.likes.BoardLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardLikesRepository extends JpaRepository<BoardLikes, Long> {

    @Query("select l from BoardLikes l where l.board.id = :boardId and l.user.userSeq = :userSeq")
    BoardLikes findExist(@Param("userSeq") Long userSeq, @Param("boardId") Long boardId);

}
