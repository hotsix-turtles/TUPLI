package hotsixturtles.tupli.repository;

import hotsixturtles.tupli.entity.Playlist;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlaylistRepositoryCustom {
    List<Playlist> listByHomePlaylist(Pageable pageable);
}
