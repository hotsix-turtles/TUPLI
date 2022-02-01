package hotsixturtles.tupli.repository;

import hotsixturtles.tupli.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
}
