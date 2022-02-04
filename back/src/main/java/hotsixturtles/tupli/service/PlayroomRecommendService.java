package hotsixturtles.tupli.service;

import hotsixturtles.tupli.entity.Playroom;
import hotsixturtles.tupli.repository.PlayroomRecommendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayroomRecommendService {

    private final PlayroomRecommendRepository playroomRecommendRepository;

    public List<Playroom> findPopularLikePlayroom(Integer size){
        return playroomRecommendRepository.findPopularLikePlayroom(size);
    }

    // 날짜 이용 연습 ??번
    public List<Playroom> findPopularLikePlayroomDate(Integer size){

        LocalDate today = LocalDate.now( ZoneOffset.UTC);

        OffsetDateTime nowTime = today.minusDays(7).atTime(OffsetTime.MIN);

        return playroomRecommendRepository.findPopularLikePlayroomDate(size, nowTime);
    }

    public List<Playroom> findFolloweesMakePlayroom(Long userSeq){
        return playroomRecommendRepository.findFolloweesMakePlayroom(userSeq);
    }

    public List<Playroom> findFolloweesLikePlayroom(Long userSeq){
        return playroomRecommendRepository.findFolloweesLikePlayroom(userSeq);
    }
}
