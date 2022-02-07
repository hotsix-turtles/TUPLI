package hotsixturtles.tupli.repository;

import hotsixturtles.tupli.entity.PlaylistComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaylistCommentRepository extends JpaRepository<PlaylistComment, Long> {

    @Query("select c from PlaylistComment c join fetch c.playlist p where p.id = :playlistId order by c.id desc")
    List<PlaylistComment> findAllByPlaylistId(@Param("playlistId") Long playlistId);
}
