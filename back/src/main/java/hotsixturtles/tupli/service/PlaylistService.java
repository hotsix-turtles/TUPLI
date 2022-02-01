package hotsixturtles.tupli.service;

import hotsixturtles.tupli.entity.Board;
import hotsixturtles.tupli.entity.Playlist;
import hotsixturtles.tupli.entity.likes.BoardLikes;
import hotsixturtles.tupli.entity.likes.PlaylistLikes;
import hotsixturtles.tupli.repository.PlaylistRepository;
import hotsixturtles.tupli.repository.UserRepository;
import hotsixturtles.tupli.repository.likes.PlaylistLikesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlaylistService {

    private final UserRepository userRepository;
    private final PlaylistRepository playlistRepository;
    private final PlaylistLikesRepository playlistLikesRepository;

    // 단일 Playlist 추가
    @Transactional
    public void addPlaylist(Long userSeq, Playlist playlist) {
        playlist.setUser(userRepository.findByUserSeq(userSeq));
        // $$$$$ 유튜브 비디오와의 연결은 프론트에서 뭐가 오냐에 따라 조금 갈림.


        playlistRepository.save(playlist);
    }

    // 단일 Playlist id로 검색
    public Playlist getPlaylist(Long playlistId) {
        return playlistRepository.findById(playlistId).orElse(null);
    }

    // 단일 Playlist id로 update
    @Transactional
    public void updatePlaylist(Long playlistId, Playlist playlistChange) {
        Playlist playlistUpdate = playlistRepository.findById(playlistId).orElse(null);

        if (playlistUpdate!= null) {
            playlistUpdate.setName(playlistChange.getName());
            playlistUpdate.setDescription(playlistChange.getDescription());
            playlistUpdate.setImage(playlistChange.getImage());
            // $$$$$ 유튜브 비디오와의 연결은 프론트에서 뭐가 오냐에 따라 조금 갈림..

            playlistRepository.save(playlistUpdate);
        }
    }

    // 단일 Playlist id로 delete
    @Transactional
    public void deletePlaylist(Long playlistId) {
        playlistRepository.deleteById(playlistId);
    }

    // 사용자가 해당 플레이리스트 좋아요 했는지.
    public PlaylistLikes getPlaylistLike(Long userSeq, Long id) {
        return playlistLikesRepository.findExist(userSeq, id);
    }

    // 사용자가 해당 플레이리스트에 좋아요
    @Transactional
    public void addPlaylistLike(Long userSeq, Long id) {
        PlaylistLikes playlistLikes = playlistLikesRepository.findExist(userSeq, id);
        if(playlistLikes == null) {
            playlistLikes = new PlaylistLikes();
            playlistLikes.setUser(userRepository.findByUserSeq(userSeq));
            playlistLikes.setPlaylist(playlistRepository.findById(id).orElse(null));
            playlistLikesRepository.save(playlistLikes);
        } else {
            // exception 발생
        }
    }

    // 사용자가 해당 플레이리스트에 좋아요 취소
    @Transactional
    public void deletePlaylistLike(Long userSeq, Long id) {
        PlaylistLikes playlistLikes = playlistLikesRepository.findExist(userSeq, id);
        if(playlistLikes != null) {
            playlistLikesRepository.delete(playlistLikes);
        } else {
            // exception 발생
        }

    }

}
