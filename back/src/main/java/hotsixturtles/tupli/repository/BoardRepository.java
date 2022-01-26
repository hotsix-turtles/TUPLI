package hotsixturtles.tupli.repository;

import hotsixturtles.tupli.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
