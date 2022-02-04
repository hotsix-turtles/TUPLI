package hotsixturtles.tupli.service;

import hotsixturtles.tupli.entity.Playlist;
import hotsixturtles.tupli.repository.PlaylistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * 유저, 플레이리스트별 추천 고도화
 */
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FlaskService {

    @Value("${flask.baseurl}")
    private String baseurl;

    @Value("${flask.enabled}")
    private Boolean enabled;

    private final PlaylistRepository playlistRepository;

    /**
     * 비동기 동작, 플레이리스트 3만건 기준으로 약 6초 소비.
     * @param playlist
     */
    @Async
    @Transactional
    public void recommendPlaylistFlask(Playlist playlist) {

        RestTemplate restTemplate = new RestTemplate();

        String url = baseurl +"/db/"+ playlist.getId();
        Long[] recommends = restTemplate.getForObject(url, Long[].class);
        List<Long> lists = Arrays.asList(recommends);
        playlist.setRecommendPlaylists(lists);
        playlistRepository.save(playlist);

    }

}
