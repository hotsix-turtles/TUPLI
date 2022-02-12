package hotsixturtles.tupli.service;

import hotsixturtles.tupli.dto.simple.home.SimpleHomeBoardDto;
import hotsixturtles.tupli.dto.simple.home.SimpleHomePlaylistDto;
import hotsixturtles.tupli.dto.simple.home.SimpleHomePlayroomDto;
import hotsixturtles.tupli.entity.HomeInfo;
import hotsixturtles.tupli.repository.BoardRepository;
import hotsixturtles.tupli.repository.HomeInfoRepository;
import hotsixturtles.tupli.repository.PlaylistRepository;
import hotsixturtles.tupli.repository.PlayroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeInfoService {

    private final HomeInfoRepository homeInfoRepository;
    private final PlaylistRepository playlistRepository;

    private final PlayroomRepository playroomRepository;

    private final BoardRepository boardRepository;

    @Transactional
    public List<Object> getHomeInfoList(Pageable pageable){
        Page<HomeInfo> homeInfoPage = homeInfoRepository.findAll(pageable);

        List<HomeInfo> result = homeInfoPage.getContent();

        List<Object> infoResult = new ArrayList<>();
        for(HomeInfo nowHomeInfo : result){
           String type = nowHomeInfo.getType();
           if(type.equals("playlist")){
               infoResult.add(new SimpleHomePlaylistDto(playlistRepository.findById(nowHomeInfo.getInfoId()).orElse(null)));
           }
           else if(type.equals("playroom")){
               infoResult.add(new SimpleHomePlayroomDto(playroomRepository.findById(nowHomeInfo.getInfoId()).orElse(null)));
           }
           else{
               infoResult.add(new SimpleHomeBoardDto(boardRepository.findById(nowHomeInfo.getInfoId()).orElse(null)));
           }
        }

        return infoResult;

    }

    @Transactional
    public List<Object> getActivites(Long userSeq, Pageable pageable){
        Page<HomeInfo> homeInfoPage = homeInfoRepository.findByUserSeq(userSeq, pageable);

        List<HomeInfo> result = homeInfoPage.getContent();

        List<Object> infoResult = new ArrayList<>();
        for(HomeInfo nowHomeInfo : result){
            String type = nowHomeInfo.getType();
            if(type.equals("playlist")){
                infoResult.add(new SimpleHomePlaylistDto(playlistRepository.findById(nowHomeInfo.getInfoId()).orElse(null)));
            }
            else if(type.equals("playroom")){
                infoResult.add(new SimpleHomePlayroomDto(playroomRepository.findById(nowHomeInfo.getInfoId()).orElse(null)));
            }
            else{
                infoResult.add(new SimpleHomeBoardDto(boardRepository.findById(nowHomeInfo.getInfoId()).orElse(null)));
            }
        }

        return infoResult;

    }


}
