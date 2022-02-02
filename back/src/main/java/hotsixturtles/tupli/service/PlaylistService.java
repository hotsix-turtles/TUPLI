package hotsixturtles.tupli.service;

import com.nimbusds.oauth2.sdk.util.ListUtils;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hotsixturtles.tupli.dto.PlaylistDto;
import hotsixturtles.tupli.dto.param.SimpleCondition;
import hotsixturtles.tupli.entity.Playlist;
import hotsixturtles.tupli.entity.QPlaylist;
import hotsixturtles.tupli.entity.likes.PlaylistLikes;
import hotsixturtles.tupli.repository.PlaylistRepository;
import hotsixturtles.tupli.repository.UserRepository;
import hotsixturtles.tupli.repository.likes.PlaylistLikesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static hotsixturtles.tupli.entity.QPlaylist.*;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlaylistService {

    private final UserRepository userRepository;
    private final PlaylistRepository playlistRepository;
    private final PlaylistLikesRepository playlistLikesRepository;

    // 심플 querydsl
    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager entityManager;


    // 단일 Playlist 추가
    @Transactional
    public void addPlaylist(Long userSeq, Playlist playlist) {
        playlist.setUser(userRepository.findByUserSeq(userSeq));
        // $$$$$ 유튜브 비디오와의 연결은 프론트에서 뭐가 오냐에 따라 조금 갈림.


        playlistRepository.save(playlist);
    }

    // 단일 Playlist id로 검색
    public Playlist getPlaylist(Long playlistId) {
        return playlistRepository.findById(playlistId).orElse(null);
    }

    // 단일 Playlist id로 update
    @Transactional
    public void updatePlaylist(Long playlistId, Playlist playlistChange) {
        Playlist playlistUpdate = playlistRepository.findById(playlistId).orElse(null);

        if (playlistUpdate!= null) {
            playlistUpdate.setName(playlistChange.getName());
            playlistUpdate.setDescription(playlistChange.getDescription());
            playlistUpdate.setImage(playlistChange.getImage());
            // $$$$$ 유튜브 비디오와의 연결은 프론트에서 뭐가 오냐에 따라 조금 갈림..

            playlistRepository.save(playlistUpdate);
        }
    }

    // 단일 Playlist id로 delete
    @Transactional
    public void deletePlaylist(Long playlistId) {
        playlistRepository.deleteById(playlistId);
    }

    // 사용자가 해당 플레이리스트 좋아요 했는지.
    public PlaylistLikes getPlaylistLike(Long userSeq, Long id) {
        return playlistLikesRepository.findExist(userSeq, id);
    }

    // 사용자가 해당 플레이리스트에 좋아요
    @Transactional
    public void addPlaylistLike(Long userSeq, Long id) {
        PlaylistLikes playlistLikes = playlistLikesRepository.findExist(userSeq, id);
        if(playlistLikes == null) {
            playlistLikes = new PlaylistLikes();
            playlistLikes.setUser(userRepository.findByUserSeq(userSeq));
            playlistLikes.setPlaylist(playlistRepository.findById(id).orElse(null));
            playlistLikesRepository.save(playlistLikes);
        } else {
            // exception 발생
        }
    }

    // 사용자가 해당 플레이리스트에 좋아요 취소
    @Transactional
    public void deletePlaylistLike(Long userSeq, Long id) {
        PlaylistLikes playlistLikes = playlistLikesRepository.findExist(userSeq, id);
        if(playlistLikes != null) {
            playlistLikesRepository.delete(playlistLikes);
        } else {
            // exception 발생
        }

    }

    // Querydsl 버전 (굳이 querydsl로 작성할 이유 하나도 없는 쿼리)
    public List<Playlist> searchPlaylistSimple(SimpleCondition condition) {
        // QueryProjection(select에 Q타입 담기) 쓰고 싶으면 다음 기회에!
        // QPlaylist.playlist를 -> playlist로 static import 했습니다.(강민구)
        // $$$$ NumberExpression 사용금지!
        // 이유 반환이 Tuple인데 변환하려면 전용 생성자 필요해서 DTO가 더러워짐, 다른 방법 아시는 분 말씀해주심 감사!
                
        // 기본값 : 관련도 순 (이름순, 작성자, 내용 순 + 나중에 이름 정해졌을때 조건 변경 필요 )
        if (condition.getType() == null | condition.getType() == "관련도순타입명설정필요" ) {

            List<Playlist> list1 = jpaQueryFactory
                    .select(playlist)
                    .from(playlist)
                    .where(titleContains(condition.getKeyword()))
                    .orderBy(playlist.id.desc())
                    .fetch();
            List<Playlist> list2 = jpaQueryFactory
                    .select(playlist)
                    .from(playlist)
                    .where(usernameContains(condition.getKeyword()))
                    .orderBy(playlist.id.desc())
                    .fetch();
            List<Playlist> list3 = jpaQueryFactory
                    .select(playlist)
                    .from(playlist)
                    .where(descriptionContains(condition.getKeyword()))
                    .orderBy(playlist.id.desc())
                    .fetch();

            // 중복 항목 제거하고 합치기.
            list2.removeAll(list1);
            list1.addAll(list2);
            list3.removeAll(list1);
            list1.addAll(list3);

            return list1;
        }
        // 다른 타입 : 최신순
        else {
            return jpaQueryFactory
                    .selectFrom(playlist)
                    .where(titleContains(condition.getKeyword())
                            .or(usernameContains(condition.getKeyword()))
                            .or(descriptionContains(condition.getKeyword())))
                    .orderBy(playlist.id.desc())
                    .fetch();
        }
    }

    //인피니티 스크롤 대비 연습용 (현재 미사용)
    public List<Playlist> searchPlaylistPage(SimpleCondition condition, Pageable pageable) {

        // 기본값 : 관련도 순 (이름순, 작성자, 내용 순 + 나중에 이름 정해졌을때 조건 변경 필요 )
        if (condition.getType() == null | condition.getType() == "관련도순타입명설정필요" ) {
            JPAQuery<Playlist> query = jpaQueryFactory
                    .selectFrom(playlist)
                    .where(titleContains(condition.getKeyword())
                            .or(usernameContains(condition.getKeyword()))
                            .or(descriptionContains(condition.getKeyword())))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize());

            //이거 끝나면 query의 sort 완료됨
            for (Sort.Order o : pageable.getSort()) {
                PathBuilder pathBuilder = new PathBuilder(playlist.getType(), playlist.getMetadata());
                query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())));
            }

            List<Playlist> result = query.fetch();
            return result;
        }
        // 다른 타입 : 최신순
        else {
            return jpaQueryFactory
                    .selectFrom(playlist)
                    .where(titleContains(condition.getKeyword())
                            .or(usernameContains(condition.getKeyword()))
                            .or(descriptionContains(condition.getKeyword())))
                    .orderBy(playlist.id.desc())
                    .fetch();
        }
    }

    // 빌더와 다르게 재사용 가능해서 좋아함
    private BooleanExpression titleContains(String keyword) {
        return hasText(keyword) ? playlist.name.contains(keyword) : null;
    }
    private BooleanExpression usernameContains(String keyword) {
        return hasText(keyword) ? playlist.user.username.contains(keyword) : null;
    }
    private BooleanExpression descriptionContains(String keyword) {
        return hasText(keyword) ? playlist.description.contains(keyword) : null;
    }

}