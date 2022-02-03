package hotsixturtles.tupli.service;

import hotsixturtles.tupli.entity.youtube.YoutubeVideo;
import hotsixturtles.tupli.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class YoutubeVideoServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    YoutubeVideoService youtubeVideoService;

    @Test
    public void 영상저장좋아요여부리스트() {
        Long userSeq = 1L;
        List<String> urls = Arrays.asList("뭐왜뭐", "난아님", "뭐왜뭐-019");

        youtubeVideoService.getSearchResultInfo(userSeq, urls);
    }

}