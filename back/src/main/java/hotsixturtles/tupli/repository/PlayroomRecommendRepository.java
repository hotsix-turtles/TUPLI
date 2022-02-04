package hotsixturtles.tupli.repository;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hotsixturtles.tupli.entity.Playroom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

import static hotsixturtles.tupli.entity.QPlayroom.playroom;
import static hotsixturtles.tupli.entity.QUser.user;
import static hotsixturtles.tupli.entity.likes.QPlayroomLikes.playroomLikes;
import static hotsixturtles.tupli.entity.likes.QUserLikes.userLikes;

@Repository
@RequiredArgsConstructor
public class PlayroomRecommendRepository {

    private final JPAQueryFactory jpaQueryFactory;

    // 1번
    public List<Playroom> findFolloweesLikePlayroom(Long userSeq){
        return jpaQueryFactory
                .selectFrom(playroom)
                .where(playroom.id.in(
                        JPAExpressions.select(playroomLikes.playroom.id)
                                .from(playroomLikes)
                                .join(userLikes)
                                .on(userLikes.toUser.userSeq.eq(playroomLikes.user.userSeq))
                                .where(userLikes.fromUser.userSeq.eq(userSeq))
                ))
                .orderBy(playroom.userCount.desc())
                .fetch();
    }

    // 2번
    public List<Playroom> findFolloweesMakePlayroom(Long userSeq){
//        return jpaQueryFactory
//                .selectFrom(playlist)
//                .join(userLikes).fetchJoin()
//                .on(playlist.user.userSeq.eq(userLikes.toUser.userSeq))
//                .where(userLikes.fromUser.userSeq.eq(userSeq))
//                .fetch();

        // 최적화: fetch join 못쓸까
        return jpaQueryFactory
                .select(playroom)
                .from(userLikes)
                .join(userLikes.toUser, user)
                .join(user.playroom, playroom)
                .where(userLikes.fromUser.userSeq.eq(userSeq))
                .orderBy(playroom.userCount.desc())
                .limit(5)
                .fetch();
    }

    // 4번
    public List<Playroom> findPopularLikePlayroom(Integer size){
        return jpaQueryFactory
                .select(playroom)
                .from(playroom)
                .orderBy(playroom.userCount.desc())
                .limit(size)
                .fetch();
    }

    // ??번 시간 실험
    public List<Playroom> findPopularLikePlayroomDate(Integer size, OffsetDateTime nowTime){
        return jpaQueryFactory
                .select(playroom)
                .from(playroom)
                .where(playroom.roomStartTime.gt(nowTime))
                .orderBy(playroom.userCount.desc())
                .limit(size)
                .fetch();
    }
}
