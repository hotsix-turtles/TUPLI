package hotsixturtles.tupli.repository;

import hotsixturtles.tupli.entity.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
    Badge findByBadgeSeq(Long badgeSeq);
    Badge findByBadgeName(String badgeName);
}