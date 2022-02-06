package hotsixturtles.tupli.repository;


import hotsixturtles.tupli.entity.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {
    UserBadge findByBadgeSeqAndUserSeq(Long badgeSeq, Long userSeq);
    List<UserBadge> findAllByUserSeq(Long userSeq);
    void deleteAllByUserSeq(Long userSeq);
}
