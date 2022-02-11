package hotsixturtles.tupli.repository;

import hotsixturtles.tupli.entity.User;
import hotsixturtles.tupli.entity.UserSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSettingRepository extends JpaRepository<UserSetting, Long> {
    UserSetting findByUser(User user);
}
