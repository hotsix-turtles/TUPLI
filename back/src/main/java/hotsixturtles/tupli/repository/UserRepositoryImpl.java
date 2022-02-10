package hotsixturtles.tupli.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hotsixturtles.tupli.dto.params.UserSearchCondition;
import hotsixturtles.tupli.entity.Playroom;
import hotsixturtles.tupli.entity.User;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

import static hotsixturtles.tupli.entity.QPlayroom.playroom;
import static hotsixturtles.tupli.entity.QUser.user;
import static org.springframework.util.StringUtils.hasText;


@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<User> searchByPageSimpleUser(UserSearchCondition condition, String order, Pageable pageable){

        if(order.equals("relevance")) {
            JPAQuery<User> query = jpaQueryFactory
                    .select(user)
                    .distinct()
                    .from(user)
                    .where(nicknameContains(condition.getKeyword())
                            .or(usernameContains(condition.getKeyword()))
                            .or(emailContains(condition.getKeyword())))
                    .orderBy(user.nickname.asc()
                            ,user.username.asc()
                            ,user.email.asc()
                            ,user.createdAt.desc()
                    )
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize());

            List<User> result = query.fetch();
            return result;
        }
        else{
            JPAQuery<User> query = jpaQueryFactory
                    .select(user)
                    .distinct()
                    .from(user)
                    .where(nicknameContains(condition.getKeyword())
                            .or(usernameContains(condition.getKeyword()))
                            .or(emailContains(condition.getKeyword())))
                    .orderBy(user.to_user.size().desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize());

            List<User> result = query.fetch();
            return result;
        }
    }


    private BooleanExpression eqEmail(String email){
        if(StringUtils.isBlank(email)){
            return null;
        }
        return user.email.eq(email);
    }

    private BooleanExpression nicknameContains(String keyword) {
        return hasText(keyword) ? user.nickname.contains(keyword) : null;
    }
    private BooleanExpression usernameContains(String keyword) {
        return hasText(keyword) ? user.username.contains(keyword) : null;
    }
    private BooleanExpression emailContains(String keyword) {
        return hasText(keyword) ? user.email.contains(keyword) : null;
    }
}
