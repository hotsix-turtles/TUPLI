package hotsixturtles.tupli.repository;

import hotsixturtles.tupli.entity.HomeInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HomeInfoRepository extends JpaRepository<HomeInfo, Long> {

    Page<HomeInfo> findAll(Pageable pageable);
    void deleteByInfoId(Long infoId);
}
