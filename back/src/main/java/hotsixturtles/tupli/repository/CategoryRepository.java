package hotsixturtles.tupli.repository;

import hotsixturtles.tupli.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllBySort(String sort);
}
