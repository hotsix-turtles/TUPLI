package hotsixturtles.tupli.entity;

import lombok.*;
import org.checkerframework.common.aliasing.qual.Unique;

import javax.persistence.*;

@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue
    private Long id;

    @Unique
    private Long categoryId;

    private String name;

    private String sort;
}
