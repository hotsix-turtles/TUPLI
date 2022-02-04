package hotsixturtles.tupli.repository;

import hotsixturtles.tupli.entity.likes.UserLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserLikesRepository extends JpaRepository<UserLikes, Long> {

    @Query("select l from UserLikes l where l.fromUser.userSeq = :userSeq and l.toUser.userSeq = :otherUserSeq")
    UserLikes findExist(@Param("userSeq") Long userSeq, @Param("otherUserSeq") Long otherUserSeq);

    @Query("select l from UserLikes l where l.toUser.userSeq = :otherUserSeq")
    List<UserLikes> findFollowers(@Param("otherUserSeq") Long otherUserSeq);
}
