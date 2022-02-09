package hotsixturtles.tupli.repository;

import hotsixturtles.tupli.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> findAllBySort(String sort);
}
