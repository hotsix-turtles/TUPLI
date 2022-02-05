package hotsixturtles.tupli.repository.likes;

import hotsixturtles.tupli.entity.likes.PlaylistLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlaylistLikesRepository extends JpaRepository<PlaylistLikes, Long> {

    @Query("select l from PlaylistLikes l where l.playlist.id = :id and l.user.userSeq = :userSeq")
    PlaylistLikes findExist(@Param("userSeq") Long userSeq, @Param("id") Long id);

}
