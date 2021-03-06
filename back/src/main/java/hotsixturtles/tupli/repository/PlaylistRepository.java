package hotsixturtles.tupli.repository;

import hotsixturtles.tupli.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    @Query("select count(p) from Playlist p join PlaylistLikes pl on p.id = pl.playlist.id " +
            "where pl.playlist.id in (select pll.id from PlaylistLikes pll where pll.user.userSeq = :userSeq)")
    Long findPlaylistLikes(@Param("userSeq") Long userSeq);

    @Query("select count(p) from Playlist p where p.user.userSeq = :userSeq")
    Long findCountByUser(@Param("userSeq") Long userSeq);

    @Query("select p from Playlist p join PlaylistLikes pl on p.id = pl.playlist.id " +
            "where pl.user.userSeq = :userSeq")
    List<Playlist> findLikedPlaylists(@Param("userSeq") Long userSeq);
}
