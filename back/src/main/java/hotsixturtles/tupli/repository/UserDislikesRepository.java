package hotsixturtles.tupli.repository;

import hotsixturtles.tupli.entity.likes.UserDislikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserDislikesRepository extends JpaRepository<UserDislikes, Long> {
    @Query("select l from UserDislikes l where l.fromUser.userSeq = :userSeq and l.toUser.userSeq = :otherUserSeq")
    UserDislikes findExist(@Param("userSeq") Long userSeq, @Param("otherUserSeq") Long otherUserSeq);
}
