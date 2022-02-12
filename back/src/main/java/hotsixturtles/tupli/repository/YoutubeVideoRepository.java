package hotsixturtles.tupli.repository;

import hotsixturtles.tupli.entity.youtube.YoutubeVideo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface YoutubeVideoRepository extends JpaRepository<YoutubeVideo, Long> {

    YoutubeVideo findByVideoId(String url);

    YoutubeVideo findFirstByVideoId(String url);

    YoutubeVideo findFirstByVideoIdOrderByIdDesc(String url);

    @Query("select y from YoutubeVideo y join YoutubeVideoLikes yl on y.id = yl.youtubeVideo.id " +
            "where yl.user.userSeq = :userSeq")
    List<YoutubeVideo> findLikedVideos(@Param("userSeq") Long userSeq);

    @Query("select y from YoutubeVideo y join YoutubeVideoSaves ys on y.id = ys.youtubeVideo.id " +
            "where ys.user.userSeq = :userSeq")
    List<YoutubeVideo> findSavedVideos(@Param("userSeq") Long userSeq);

}
