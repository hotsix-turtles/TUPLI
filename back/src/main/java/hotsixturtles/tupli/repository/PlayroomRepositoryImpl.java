package hotsixturtles.tupli.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hotsixturtles.tupli.dto.params.PlayroomSearchCondition;
import hotsixturtles.tupli.entity.Playroom;
import hotsixturtles.tupli.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

import static hotsixturtles.tupli.entity.QPlayroom.playroom;

@Repository
@RequiredArgsConstructor
public class PlayroomRepositoryImpl implements PlayroomRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Playroom> searchByPageSimplePlayroom(PlayroomSearchCondition playroomSearchCondition,
                                                     Pageable pageable){

        JPAQuery<Playroom> query = jpaQueryFactory
                .selectFrom(playroom)
                .where(
                        playroom.roomTitle.contains(playroomSearchCondition.getKeyword())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(playroom.getType(), playroom.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        List<Playroom> result = query.fetch();
        return result;
    }

}
