package hotsixturtles.tupli.repository.likes;

import hotsixturtles.tupli.entity.User;
import hotsixturtles.tupli.entity.likes.YoutubeVideoLikes;
import hotsixturtles.tupli.entity.likes.YoutubeVideoSaves;
import hotsixturtles.tupli.entity.youtube.YoutubeVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface YoutubeVideoSavesRepository extends JpaRepository<YoutubeVideoSaves, Long> {

    @Query("select s from YoutubeVideoSaves s where s.youtubeVideo.id = :youtubeVideoId and s.user.userSeq = :userSeq")
    YoutubeVideoSaves findExist(@Param("userSeq") Long userSeq, @Param("youtubeVideoId") Long youtubeVideoId);

    // url로 존재하나 확인
    @Query("select s from YoutubeVideoSaves s where s.youtubeVideo.videoId = :url and s.user.userSeq = :userSeq")
    YoutubeVideoSaves findExistByUrl(@Param("userSeq") Long userSeq, @Param("url") String url);

    // 해당 유저가 저장한 영상 전부
    @Query("select s.youtubeVideo from YoutubeVideoSaves s where s.user = :user order by s.id desc")
    List<YoutubeVideo> findByUserOrderByIdDesc(@Param("user") User user);

    // 해당 영상 저장했는지 여부
    @Query("select count(s.id) > 0 from YoutubeVideoSaves s where s.youtubeVideo.videoId = :url and s.user.userSeq = :userSeq")
    boolean isUserSaved (@Param("userSeq") Long userSeq, @Param("url") String url);

}
