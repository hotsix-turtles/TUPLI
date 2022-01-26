package hotsixturtles.tupli.repository;

import hotsixturtles.tupli.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    UserInfo findOneByUserSeq(Long userSeq);

}
