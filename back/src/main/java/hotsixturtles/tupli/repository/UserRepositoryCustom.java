package hotsixturtles.tupli.repository;

import hotsixturtles.tupli.dto.params.UserSearchCondition;
import hotsixturtles.tupli.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepositoryCustom {
    List<User> searchByPageSimpleUser(UserSearchCondition userSearchCondition,String order, Pageable pageable);
}
