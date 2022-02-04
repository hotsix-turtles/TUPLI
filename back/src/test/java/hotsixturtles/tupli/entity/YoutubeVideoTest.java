package hotsixturtles.tupli.entity;

import hotsixturtles.tupli.dto.simple.SimpleYoutubeVideoDto;
import hotsixturtles.tupli.entity.auth.ProviderType;
import hotsixturtles.tupli.entity.auth.RoleType;
import hotsixturtles.tupli.entity.youtube.YoutubeVideo;
import hotsixturtles.tupli.repository.UserRepository;
import hotsixturtles.tupli.service.YoutubeVideoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class YoutubeVideoTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    YoutubeVideoService youtubeVideoService;

    @Test
    @Rollback(value = false)
    public void 영상생성저장() {
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
            SimpleYoutubeVideoDto youtubeVideoDto = new SimpleYoutubeVideoDto();
            youtubeVideoDto.setVideoId("뭐왜뭐-0"+i);
            youtubeVideoDto.setCategoryId((int) (1 + Math.random() * 20));

            //영상 추가 or 검색 후 영상 저장 (Transaction상 분리, 합치지 말 것)
            YoutubeVideo youtubeVideo = youtubeVideoService.addVideo(youtubeVideoDto);
            youtubeVideoService.saveVideo(1L, youtubeVideo);

        }
    }

}