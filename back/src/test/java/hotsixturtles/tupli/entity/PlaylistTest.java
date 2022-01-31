package hotsixturtles.tupli.entity;

import hotsixturtles.tupli.entity.auth.ProviderType;
import hotsixturtles.tupli.entity.auth.RoleType;
import hotsixturtles.tupli.entity.meta.PlaylistInfo;
import hotsixturtles.tupli.repository.PlaylistRepository;
import hotsixturtles.tupli.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PlaylistTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PlaylistRepository playlistRepository;

    @Test
    @Rollback(value = false)
    public void 플레이리스트샘플생성() {
        User user = userRepository.findByUserSeq(1L);
        if (user == null) {
            user = new User();
            user.setUsername("테스트유저");
            user.setEmail("테스트유저");
            user.setPassword("111");
            user.setNickname("테스트유저");
            user.setProviderType(ProviderType.LOCAL);
            user.setEmailVerifiedYn("N");
            user.setCreatedAt(LocalDateTime.now());
            user.setModifiedAt(LocalDateTime.now());
            user.setRoleType(RoleType.USER);
            user.setIs_vip("N");
            userRepository.save(user);
        }

        for (int i = 0; i < 20; i++) {
            PlaylistInfo playlistInfo = new PlaylistInfo();
            playlistInfo.setTag01((int) (1 + Math.random() * 4));
            playlistInfo.setTag02((int) (1 + Math.random() * 4));
            playlistInfo.setTag10((int) (1 + Math.random() * 4));
            playlistInfo.setTag15((int) (1 + Math.random() * 4));

            Playlist playlist = new Playlist();
            playlist.setName("test" + i);
            playlist.setUser(user);
            playlist.setPlaylistInfo(playlistInfo);

            playlistRepository.save(playlist);
        }
    }
}