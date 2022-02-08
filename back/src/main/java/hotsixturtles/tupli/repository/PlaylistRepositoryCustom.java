package hotsixturtles.tupli.repository;

import hotsixturtles.tupli.dto.params.PlaylistSearchCondition;
import hotsixturtles.tupli.entity.Playlist;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlaylistRepositoryCustom {
    List<Playlist> searchByPageSimplePlaylist(PlaylistSearchCondition playlistSearchCondition, Pageable pageable);
    List<Playlist> listByHomePlaylist(Pageable pageable);

    List<Playlist> categorizedByPageSimplePlaylist(String category, Pageable pageable);
}
