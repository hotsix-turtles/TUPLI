package hotsixturtles.tupli.repository;

import hotsixturtles.tupli.dto.params.PlayroomSearchCondition;
import hotsixturtles.tupli.entity.Playroom;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlayroomRepositoryCustom {
    List<Playroom> searchByPageSimplePlayroom(PlayroomSearchCondition playroomSearchCondition, Pageable pageable);
    List<Playroom> listByHomePlayroom( Pageable pageable);
}
