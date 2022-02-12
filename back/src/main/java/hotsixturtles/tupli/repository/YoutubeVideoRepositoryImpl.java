package hotsixturtles.tupli.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hotsixturtles.tupli.dto.params.VideoSearchCondition;
import hotsixturtles.tupli.entity.youtube.YoutubeVideo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

import static hotsixturtles.tupli.entity.youtube.QYoutubeVideo.youtubeVideo;

@Repository
@RequiredArgsConstructor
public class YoutubeVideoRepositoryImpl implements YoutubeVideoRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<YoutubeVideo> searchByPageSimpleVideo(VideoSearchCondition videoSearchCondition, Pageable pageable) {

            JPAQuery<YoutubeVideo> query = jpaQueryFactory
                    .selectFrom(youtubeVideo)
                    .where(
                            youtubeVideo.title.contains(videoSearchCondition.getKeyword())
                    )
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize());

            for (Sort.Order o : pageable.getSort()) {
                PathBuilder pathBuilder = new PathBuilder(youtubeVideo.getType(), youtubeVideo.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            }

            List<YoutubeVideo> result = query.fetch();
            return result;
    }

    @Override
    public List<YoutubeVideo> searchNoConditionByPageVideo(Pageable pageable) {

        JPAQuery<YoutubeVideo> query = jpaQueryFactory
                .selectFrom(youtubeVideo)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(youtubeVideo.getType(), youtubeVideo.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        List<YoutubeVideo> result = query.fetch();
        return result;
    }
    @Override
    public List<YoutubeVideo> findDistinctByVideoId(Pageable pageable) {

        JPAQuery<YoutubeVideo> query = jpaQueryFactory
                .select(youtubeVideo)
                .distinct()
                .from(youtubeVideo)
                .groupBy(youtubeVideo.videoId)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(youtubeVideo.getType(), youtubeVideo.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        List<YoutubeVideo> result = query.fetch();
        return result;
    }

}
