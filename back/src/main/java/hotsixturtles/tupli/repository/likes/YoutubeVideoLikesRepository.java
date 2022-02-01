package hotsixturtles.tupli.repository.likes;

import hotsixturtles.tupli.entity.User;
import hotsixturtles.tupli.entity.likes.YoutubeVideoLikes;
import hotsixturtles.tupli.entity.likes.YoutubeVideoSaves;
import hotsixturtles.tupli.entity.youtube.YoutubeVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface YoutubeVideoLikesRepository extends JpaRepository<YoutubeVideoLikes, Long> {

    @Query("select l from YoutubeVideoLikes l where l.youtubeVideo.id = :youtubeVideoId and l.user.userSeq = :userSeq")
    YoutubeVideoLikes findExist(@Param("userSeq") Long userSeq, @Param("youtubeVideoId") Long youtubeVideoId);

    // url로 존재하나 확인
    @Query("select l from YoutubeVideoLikes l where l.youtubeVideo.url = :url and l.user.userSeq = :userSeq")
    YoutubeVideoLikes findExistByUrl(Long userSeq, String url);

    // 해당 유저가 좋아요 영상 전부
    @Query("select l.youtubeVideo from YoutubeVideoLikes l where l.user = :user order by l.id desc")
    List<YoutubeVideo> findByUserOrderByIdDesc(@Param("user") User user);

    // 해당 영상 좋아요했는지 여부
    @Query("select count(l.id) > 0 from YoutubeVideoLikes l where l.youtubeVideo.url = :url and l.user.userSeq = :userSeq")
    boolean isUserLikes (@Param("userSeq") Long userSeq, @Param("url") String url);

}
