package hotsixturtles.tupli.service;

import hotsixturtles.tupli.dto.simple.SimpleYoutubeVideoDto;
import hotsixturtles.tupli.dto.simple.YoutubeVideoLikesSavedDto;
import hotsixturtles.tupli.entity.likes.YoutubeVideoLikes;
import hotsixturtles.tupli.entity.likes.YoutubeVideoSaves;
import hotsixturtles.tupli.entity.youtube.YoutubeVideo;
import hotsixturtles.tupli.repository.*;
import hotsixturtles.tupli.repository.likes.YoutubeVideoLikesRepository;
import hotsixturtles.tupli.repository.likes.YoutubeVideoSavesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class YoutubeVideoService {

    private final UserRepository userRepository;
    private final YoutubeVideoRepository youtubeVideoRepository;

    private final YoutubeVideoLikesRepository youtubeVideoLikesRepository;
    private final YoutubeVideoSavesRepository youtubeVideoSavesRepository;


    @Transactional
    public YoutubeVideo addVideo(SimpleYoutubeVideoDto youtubeVideoDto) {
        // 있을 경우 탐색, 없을 경우 DB에 추가.
        YoutubeVideo youtubeVideo = youtubeVideoRepository.findByVideoId(youtubeVideoDto.getVideoId());
        if(youtubeVideo == null) {
            youtubeVideo = new YoutubeVideo();
            youtubeVideo.setInit(youtubeVideoDto);  // Custom setter
            youtubeVideoRepository.save(youtubeVideo);
        } else {
            // 만약 정보 업데이트 넣을거면 여기
        }
        return youtubeVideo;
    }

    @Transactional
    public void saveVideo(Long userSeq, YoutubeVideo youtubeVideo) {
        // 해당 유저가 기존에 저장 했나 확인, 없을 경우 저장
        YoutubeVideoSaves existVideo = youtubeVideoSavesRepository.findExist(userSeq, youtubeVideo.getId());
        if(existVideo == null) {
            YoutubeVideoSaves youtubeVideoSaves = new YoutubeVideoSaves();
            youtubeVideoSaves.setUser(userRepository.findByUserSeq(userSeq));
            youtubeVideoSaves.setYoutubeVideo(youtubeVideo);
            youtubeVideoSavesRepository.save(youtubeVideoSaves);
        } else {
            // 익셉션 발생
        }
    }

    // 저장 취소. id가 아닌 url이 온다고 가정하고 설계, 
    // 기존 영상은 다른 DB에서 쓸 수 있으니 일단 삭제하지 않는다. (그냥 카운트 돌려서 0이면 지우면 되긴 함)
    @Transactional
    public void forgotVideo(Long userSeq, String url) {
        YoutubeVideoSaves video = youtubeVideoSavesRepository.findExistByUrl(userSeq, url);
        if(video != null) {
            youtubeVideoSavesRepository.delete(video);
        } else {
            // 익셉션 발생
        }
    }

    // 좋아요 취소. id가 아닌 url이 온다고 가정하고 설계,
    // 기존 영상은 다른 DB에서 쓸 수 있으니 일단 삭제하지 않는다. (그냥 카운트 돌려서 0이면 지우면 되긴 함)
    @Transactional
    public void dislikesVideo(Long userSeq, String url) {
        YoutubeVideoLikes video = youtubeVideoLikesRepository.findExistByUrl(userSeq, url);
        if(video != null) {
            youtubeVideoLikesRepository.delete(video);
        } else {
            // 익셉션 발생
        }
    }

    public YoutubeVideoLikes getLikesVideo(Long userSeq, Long videoId){
        return youtubeVideoLikesRepository.findExist(userSeq, videoId);
    }

    @Transactional
    public void likesVideo(Long userSeq, YoutubeVideo youtubeVideo) {
        // 해당 유저가 기존에 좋아요 했나 확인, 없을 경우 저장
        YoutubeVideoLikes existVideo = youtubeVideoLikesRepository.findExist(userSeq, youtubeVideo.getId());
        if(existVideo == null) {
            YoutubeVideoLikes youtubeVideoLikes = new YoutubeVideoLikes();
            youtubeVideoLikes.setUser(userRepository.findById(userSeq).orElse(null));
            youtubeVideoLikes.setYoutubeVideo(youtubeVideo);
            youtubeVideoLikesRepository.save(youtubeVideoLikes);
        } else {
            // 익셉션 발생
        }
    }

    // 저장한 영상 최대 전부 보내기
    public List<YoutubeVideo> getAllSavedVideos(Long userSeq) {
        return youtubeVideoSavesRepository.findByUserOrderByIdDesc(userRepository.findByUserSeq(userSeq));
    }

    // 좋아요 영상 최대 전부 보내기
    public List<YoutubeVideo> getAllLikesVideos(Long userSeq) {
        return youtubeVideoLikesRepository.findByUserOrderByIdDesc(userRepository.findByUserSeq(userSeq));
    }

    // 검색 결과를 기존에 저장, 좋아요 했는지 체크
    public YoutubeVideoLikesSavedDto getSearchResultInfo(Long userSeq, List<String> urls) {
        List<Boolean> is_userSaved = new ArrayList<>();
        List<Boolean> is_userLikes = new ArrayList<>();
        List<Integer> is_LikesCount = new ArrayList<>();

        for (String url : urls) {
            is_userSaved.add(youtubeVideoSavesRepository.isUserSaved(userSeq, url));
            is_userLikes.add(youtubeVideoLikesRepository.isUserLikes(userSeq, url));
            is_LikesCount.add(youtubeVideoLikesRepository.isUserLikesCnt(url));
        }

        YoutubeVideoLikesSavedDto dto = new YoutubeVideoLikesSavedDto();
        dto.setIsSavedList(is_userSaved);
        dto.setIsLikesList(is_userLikes);
        dto.setIsLikesCnt(is_LikesCount);

        return dto;
    }

    // 좋아요 영상 최대 전부 보내기
    public List<YoutubeVideo> getSavedVideos(Long userSeq) {
        return youtubeVideoRepository.findSavedVideos(userSeq);
    }    // 좋아요 영상 최대 전부 보내기

    public List<YoutubeVideo> getLikedVideos(Long userSeq) {
        return youtubeVideoRepository.findLikedVideos(userSeq);
    }
}
