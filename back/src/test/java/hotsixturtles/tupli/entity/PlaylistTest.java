package hotsixturtles.tupli.entity;

import hotsixturtles.tupli.dto.simple.SimpleYoutubeVideoDto;
import hotsixturtles.tupli.entity.auth.ProviderType;
import hotsixturtles.tupli.entity.auth.RoleType;
import hotsixturtles.tupli.repository.PlaylistRepository;
import hotsixturtles.tupli.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

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

        for (int i = 0; i < 27000; i++) {
            Playlist playlist = new Playlist();
            playlist.setTitle("test" + i);
            playlist.setUser(user);

            ConcurrentHashMap<Integer, Integer> playlistInfo = new ConcurrentHashMap<Integer, Integer>();
            for (int j = 0; j < (int) (2 + Math.random() * 4); j++) {
                // Playlistinfo에 따라 갈림
                Integer categoryId = (int) (1 + Math.random() * 20);
                Integer count = playlistInfo.getOrDefault(categoryId, 0);
                playlistInfo.put(categoryId, count+1);
            }
            playlist.setPlaylistInfo(playlistInfo);
            playlistRepository.save(playlist);
        }
    }
}