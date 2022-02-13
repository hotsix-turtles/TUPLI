package hotsixturtles.tupli.repository;

import hotsixturtles.tupli.entity.meta.UserInfo;
import io.lettuce.core.dynamic.annotation.Param;
import org.hibernate.sql.Update;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    UserInfo findOneByUserSeq(Long userSeq);

    void deleteByUserSeq(Long userSeq);

}
