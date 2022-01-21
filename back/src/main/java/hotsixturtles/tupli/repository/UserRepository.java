package hotsixturtles.tupli.repository;

import hotsixturtles.tupli.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
