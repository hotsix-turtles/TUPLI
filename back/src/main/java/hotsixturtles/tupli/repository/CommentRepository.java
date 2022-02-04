package hotsixturtles.tupli.repository;

import hotsixturtles.tupli.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c join fetch c.board b where b.id = :boardId order by c.id desc")
    List<Comment> findAllByBoardId(@Param("boardId") Long boardId);

//    @Query("delete from Comment c where c.board.id = :boardId")
//    void deleteAllByBoardId(@Param("boardId") Long boardId);
}
