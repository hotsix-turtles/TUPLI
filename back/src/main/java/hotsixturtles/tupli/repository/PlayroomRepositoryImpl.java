package hotsixturtles.tupli.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hotsixturtles.tupli.dto.params.PlayroomSearchCondition;
import hotsixturtles.tupli.entity.Playlist;
import hotsixturtles.tupli.entity.Playroom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

import static hotsixturtles.tupli.entity.QPlaylist.playlist;
import static hotsixturtles.tupli.entity.QPlayroom.playroom;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class PlayroomRepositoryImpl implements PlayroomRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Playroom> searchByPageSimplePlayroom(PlayroomSearchCondition condition,String order
                                                     ,Pageable pageable){
        if(order.equals("relevance")) {
            JPAQuery<Playroom> query = jpaQueryFactory
                    .select(playroom)
                    .distinct()
                    .from(playroom)
                    .where(titleContains(condition.getKeyword())
                            .or(tagContains(condition.getKeyword()))
                            .or(usernameContains(condition.getKeyword()))
                            .or(descriptionContains(condition.getKeyword())))
                    .orderBy(playroom.title.asc()
                            , playroom.user.username.asc()
                            , playroom.tags.asc()
                            , playroom.content.asc()
                    )
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize());

            List<Playroom> result = query.fetch();
            return result;
        }
        else if(order.equals("date")){
            JPAQuery<Playroom> query = jpaQueryFactory
                    .select(playroom)
                    .distinct()
                    .from(playroom)
                    .where(titleContains(condition.getKeyword())
                            .or(tagContains(condition.getKeyword()))
                            .or(usernameContains(condition.getKeyword()))
                            .or(descriptionContains(condition.getKeyword())))
                    .orderBy(playroom.startTime.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize());

            List<Playroom> result = query.fetch();
            return result;
        }
        else{
            JPAQuery<Playroom> query = jpaQueryFactory
                    .select(playroom)
                    .distinct()
                    .from(playroom)
                    .where(titleContains(condition.getKeyword())
                            .or(tagContains(condition.getKeyword()))
                            .or(usernameContains(condition.getKeyword()))
                            .or(descriptionContains(condition.getKeyword())))
                    .orderBy(playroom.likesCnt.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize());

            List<Playroom> result = query.fetch();
            return result;
        }

    }

    @Override
    public List<Playroom> listByHomePlayroom(Pageable pageable){

        JPAQuery<Playroom> query = jpaQueryFactory
                .selectFrom(playroom)
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

    @Override
    public List<Playroom> categorizedByPageSimplePlayroom(String category, Pageable pageable) {
        JPAQuery<Playroom> query = jpaQueryFactory
                .selectFrom(playroom)
                .where(
                        playroom.playroomCate.contains(category)
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

    private BooleanExpression titleContains(String keyword) {
        return hasText(keyword) ? playroom.title.contains(keyword) : null;
    }
    private BooleanExpression usernameContains(String keyword) {
        return hasText(keyword) ? playroom.user.username.contains(keyword) : null;
    }
    private BooleanExpression descriptionContains(String keyword) {
        return hasText(keyword) ? playroom.content.contains(keyword) : null;
    }
    private BooleanExpression tagContains(String keyword) {
        return hasText(keyword) ? playroom.tags.contains(keyword) : null;
    }
}
