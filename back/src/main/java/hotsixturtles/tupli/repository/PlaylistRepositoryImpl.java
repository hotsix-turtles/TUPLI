package hotsixturtles.tupli.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hotsixturtles.tupli.dto.params.PlaylistSearchCondition;
import hotsixturtles.tupli.dto.params.UserSearchCondition;
import hotsixturtles.tupli.entity.Playlist;
import hotsixturtles.tupli.entity.Playroom;
import hotsixturtles.tupli.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

import static hotsixturtles.tupli.entity.QPlaylist.playlist;
import static hotsixturtles.tupli.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class PlaylistRepositoryImpl implements PlaylistRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Playlist> searchByPageSimplePlaylist(PlaylistSearchCondition searchCondition, Pageable pageable){

        JPAQuery<Playlist> query = jpaQueryFactory
                .selectFrom(playlist)
                .where(
                        playlist.title.contains(searchCondition.getKeyword())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(playlist.getType(), playlist.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        List<Playlist> result = query.fetch();
        return result;
    }
    @Override
    public List<Playlist> listByHomePlaylist(Pageable pageable){

        JPAQuery<Playlist> query = jpaQueryFactory
                .selectFrom(playlist)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(playlist.likesCnt.desc());

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(playlist.getType(), playlist.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        List<Playlist> result = query.fetch();
        return result;
    }

    @Override
    public List<Playlist> categorizedByPageSimplePlaylist(String category, Pageable pageable) {
        JPAQuery<Playlist> query = jpaQueryFactory
                .selectFrom(playlist)
                .where(
                        playlist.playlistCate.contains(category)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(playlist.getType(), playlist.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        List<Playlist> result = query.fetch();
        return result;
    }
}
