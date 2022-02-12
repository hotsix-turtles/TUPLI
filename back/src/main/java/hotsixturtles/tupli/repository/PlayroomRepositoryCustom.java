package hotsixturtles.tupli.repository;

import hotsixturtles.tupli.dto.params.PlayroomSearchCondition;
import hotsixturtles.tupli.entity.Playroom;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlayroomRepositoryCustom {
    List<Playroom> searchByPageSimplePlayroom(PlayroomSearchCondition playroomSearchCondition, String order, Pageable pageable);
    List<Playroom> listByHomePlayroom( Pageable pageable);
    List<Playroom> categorizedByPageSimplePlayroom(String category, Pageable pageable);
}
