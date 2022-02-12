package hotsixturtles.tupli.entity;

import hotsixturtles.tupli.dto.simple.SimpleHomeInfoDto;
import lombok.*;

import javax.persistence.*;
import java.time.OffsetDateTime;


@Entity
@Table(name = "homeInfo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HomeInfo {

    @Id
    @GeneratedValue
    private Long id;

    private String type;

    // Playroom, Playlist, BoardId 저장
    private Long infoId;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    private Long userSeq;

    @PrePersist
    private void beforeSaving() {
        createdAt = OffsetDateTime.now();
    }

}
