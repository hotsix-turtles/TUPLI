package hotsixturtles.tupli.repository;

import hotsixturtles.tupli.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByUsername(String username);

    void deleteByUserSeq(Long userSeq);

    User findByEmail(String email);

    List<User> findByUsername(String username);

    User findByUserId(String userId);

    User findByUserSeq(Long userSeq);
}
