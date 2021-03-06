package hotsixturtles.tupli.repository;

import hotsixturtles.tupli.entity.Playroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface PlayroomRepository extends JpaRepository<Playroom, Long> {
    @Query("select count(p) from Playroom p where p.user.userSeq = :userSeq")
    Long findCountByUser(@Param("userSeq") Long userSeq);

    @Query("select max(p.userCount) from Playroom p where p.user.userSeq = :userSeq")
    Long findRoomMaxUser(@Param("userSeq") Long userSeq);

    @Query("select p from Playroom p join PlayroomLikes pr on p.id = pr.playroom.id " +
            "where pr.user.userSeq = :userSeq")
    List<Playroom> findLikedPlayrooms(@Param("userSeq") Long userSeq);
}
