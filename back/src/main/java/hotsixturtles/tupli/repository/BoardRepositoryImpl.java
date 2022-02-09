package hotsixturtles.tupli.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hotsixturtles.tupli.dto.params.BoardSearchCondition;
import hotsixturtles.tupli.dto.params.UserSearchCondition;
import hotsixturtles.tupli.entity.Board;
import hotsixturtles.tupli.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

import static hotsixturtles.tupli.entity.QBoard.board;
import static hotsixturtles.tupli.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Board> searchByPageSimpleBoard(BoardSearchCondition searchCondition, Pageable pageable){

        JPAQuery<Board> query = jpaQueryFactory
                .selectFrom(board)
                .where(
                        board.title.contains(searchCondition.getKeyword())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(board.getType(), board.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        List<Board> result = query.fetch();
        return result;
    }

    @Override
    public List<Board> listByHomeBoard(Pageable pageable){

        JPAQuery<Board> query = jpaQueryFactory
                .selectFrom(board)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(board.getType(), board.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        List<Board> result = query.fetch();
        return result;
    }
}
