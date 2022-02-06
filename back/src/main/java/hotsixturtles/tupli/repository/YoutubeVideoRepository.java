package hotsixturtles.tupli.repository;

import hotsixturtles.tupli.entity.youtube.YoutubeVideo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface YoutubeVideoRepository extends JpaRepository<YoutubeVideo, Long> {

    YoutubeVideo findByVideoId(String url);

    YoutubeVideo findOneByVideoId(String url);
}
