package hotsixturtles.tupli.entity;

import hotsixturtles.tupli.dto.request.PlaylistRequest;
import hotsixturtles.tupli.dto.simple.SimpleYoutubeVideoDto;
import hotsixturtles.tupli.entity.auth.ProviderType;
import hotsixturtles.tupli.entity.auth.RoleType;
import hotsixturtles.tupli.entity.youtube.YoutubeVideo;
import hotsixturtles.tupli.repository.PlaylistRepository;
import hotsixturtles.tupli.repository.UserRepository;
import hotsixturtles.tupli.service.PlaylistService;
import hotsixturtles.tupli.service.YoutubeVideoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PlaylistTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PlaylistRepository playlistRepository;

    @Autowired
    PlaylistService playlistService;

    @Autowired
    YoutubeVideoService youtubeVideoService;

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

        for (int i = 0; i < 10; i++) {
            PlaylistRequest playlistRequest = new PlaylistRequest();
            playlistRequest.setTitle("test"+i);
            playlistRequest.setContent("테스트용 플레이리스트");
            playlistRequest.setTags("신남, 즐거움, 힘듬");
            playlistRequest.setIsPublic(true);

            List<SimpleYoutubeVideoDto> videos = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                SimpleYoutubeVideoDto youtubeVideoDto = new SimpleYoutubeVideoDto();
                youtubeVideoDto.setVideoId("테스트용"+i);
                youtubeVideoDto.setCategoryId((int) (1 + Math.random() * 20));
                videos.add(youtubeVideoDto);

                //영상 추가 or 검색 후 영상 저장 (Transaction상 분리, 합치지 말 것)
                YoutubeVideo youtubeVideo = youtubeVideoService.addVideo(youtubeVideoDto);
                youtubeVideoService.saveVideo(1L, youtubeVideo);
            }
            playlistRequest.setVideos(videos);

            playlistService.addPlaylist(1L, playlistRequest);
        }
    }

    @Test
    @Rollback(value = false)
    public void 플레이리스트샘플생성Simple() {
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

        for (int i = 0; i < 10; i++) {
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
