package hotsixturtles.tupli.repository;

import hotsixturtles.tupli.dto.params.VideoSearchCondition;
import hotsixturtles.tupli.entity.youtube.YoutubeVideo;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface YoutubeVideoRepositoryCustom {
    List<YoutubeVideo> searchByPageSimpleVideo(VideoSearchCondition videoSearchCondition, Pageable pageable);
}
