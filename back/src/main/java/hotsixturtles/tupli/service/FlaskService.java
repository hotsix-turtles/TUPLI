package hotsixturtles.tupli.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
