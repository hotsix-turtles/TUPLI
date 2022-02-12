package hotsixturtles.tupli.repository.likes;

import hotsixturtles.tupli.entity.likes.PlayroomLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlayroomLikesRepository extends JpaRepository<PlayroomLikes, Long> {

    @Query("select l from PlayroomLikes l where l.playroom.id = :id and l.user.userSeq = :userSeq")
    PlayroomLikes findExist(@Param("userSeq") Long userSeq, @Param("id") Long id);

}
