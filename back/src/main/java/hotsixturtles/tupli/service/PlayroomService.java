package hotsixturtles.tupli.service;

import hotsixturtles.tupli.dto.PlayroomDto;
import hotsixturtles.tupli.dto.request.RequestPlayroomDto;
import hotsixturtles.tupli.dto.response.ResponsePlayroomDto;
import hotsixturtles.tupli.dto.simple.SimpleUserDto;
import hotsixturtles.tupli.entity.Playlist;
import hotsixturtles.tupli.entity.Playroom;
import hotsixturtles.tupli.entity.User;
import hotsixturtles.tupli.entity.likes.UserLikes;
import hotsixturtles.tupli.entity.youtube.YoutubeVideo;
import hotsixturtles.tupli.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class PlayroomService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PlayroomRepository playroomRepository;
    private final YoutubeVideoRepository youtubeVideoRepository;
    private final NotificationService notificationService;
    private final PlayroomRepositoryCustom playroomRepositoryCustom;

    public List<Playroom> getPlayroomList(){

        // Paging 조건 정해서 추가
        return playroomRepository.findAll();
    }

    // Home 화면 Playroom 검색
    public List<Playroom> getHomePlayrooms(Pageable pageable) {
        return playroomRepositoryCustom.listByHomePlayroom(pageable);
    }

    public Playroom getPlayroom(Long playroomId){

        // Paging 조건 정해서 추가
        Playroom playroom = playroomRepository.findById(playroomId).orElse(null);
        System.out.println(playroom.getPlaylists());
        return playroom;
//        return playroomRepository.findById(playroomId).orElse(null);
    }

    // 사용자가 좋아요 누른 playroom
    public List<Playroom> getLikedPlayrooms(Long userSeq){
        return playroomRepository.findLikedPlayrooms(userSeq);
    }

    @Transactional
    public PlayroomDto addPlayroom(RequestPlayroomDto playroomDto, Long userSeq){

        Playroom playroom = new Playroom();

        // 임시명칭 : 방 생성자(maker), 싱크 조절자(owner, 방장) << 현재 분리안은 고도화로 미룸
        User maker = userRepository.findByUserSeq(userSeq);
        playroom.setUser(maker);

        playroom.setTitle(playroomDto.getTitle());
        playroom.setContent(playroomDto.getContent());
        playroom.setIsPublic(playroomDto.getIsPublic());
        playroom.setTags(playroomDto.getTags());
        playroom.setEndTime(playroomDto.getEndTime());

        // 플레이리스트 비디오 분리하고 저장 + ID만 저장
        ConcurrentHashMap<Long, List<Long>> playlists = new ConcurrentHashMap<>();
        ConcurrentHashMap<Integer, Integer> playroomInfo = new ConcurrentHashMap<Integer, Integer>();
        for (Map.Entry<Long, List<String>> entry : playroomDto.getPlaylists().entrySet()) {
            // 플레이리스트 ID만 저장
//            playlists.add(entry.getKey());
            List<Long> playroomPlList = new ArrayList<>();

            // 비디오 저장(플레이리스트 삭제 대비 및 편한 추가 삭제를 위해 DB 별도 저장) 최적화 반드시 필요!!!!! $$$
            for (String videoUrl : entry.getValue()) {

                YoutubeVideo video = new YoutubeVideo();
                YoutubeVideo existVideo = youtubeVideoRepository.findFirstByVideoId(videoUrl);
                video.setPlayroom(playroom);
                video.setInit(existVideo);
                youtubeVideoRepository.save(video);
                playroomPlList.add(youtubeVideoRepository.findFirstByVideoIdOrderByIdDesc(videoUrl).getId());

                // 플레이룸 구성 비디오 정보로 메타 정보 구축
                Integer categoryId = existVideo.getCategoryId();
                Integer count = playroomInfo.getOrDefault(categoryId, 0);
                playroomInfo.put(categoryId, count+1);
            }
            playlists.put(entry.getKey(), playroomPlList);

        }
        playroom.setPlayroomInfo(playroomInfo);
        playroom.setPlaylists(playlists);
        playroomRepository.save(playroom);

        // 플레이룸 개설 알림 보내기 (초청 유저)
        for (Long inviteId : playroomDto.getInviteIds()) {
            notificationService.notiPlayroomMake(userSeq, inviteId);
        }

        // 플레이룸 개설 알림 보내기 (로직짜고, 팔로우한 유저들에게, 유저 설정 부분 만들어지면 그 때 수정)
        if (false) {
            List<UserLikes> followers = userService.getFollowers(userSeq);
            for (UserLikes follower : followers) {
                notificationService.notiPlayroomMake(userSeq, follower.getFromUser().getUserSeq());
            }

        }

        return new PlayroomDto(playroom);
    }

    @Transactional
    public PlayroomDto updatePlayroom(Long playroomId, RequestPlayroomDto playroomDto, Long userSeq){

        Playroom playroom = playroomRepository.findById(playroomId).orElse(null);

        if(playroom == null || playroom.getUser().getUserSeq() != userSeq){
            return null;
        }

        if(playroomDto.getTitle() != null){
            playroom.setTitle(playroomDto.getTitle());
        }
        if(playroomDto.getContent() != null){
            playroom.setContent(playroomDto.getContent());
        }


        playroomRepository.save(playroom);

        return new PlayroomDto(playroom);
    }

    @Transactional
    public Playroom deletePlayroom(Long playroomId, Long userSeq){

        Playroom playroom = playroomRepository.findById(playroomId).orElse(null);

        if(playroom == null || playroom.getUser().getUserSeq() != userSeq){
            return null;
        }

        playroomRepository.deleteById(playroomId);
        return playroom;
    }

    @Transactional
    public void changePlayroomUser(Long userSeq, Long playroomId) {
        Playroom playroom = playroomRepository.findById(playroomId).orElse(null);
        if (playroom != null) {
            playroom.setUser(userRepository.findByUserSeq(userSeq));
            playroomRepository.save(playroom);
        }
    }
}
