package hotsixturtles.tupli.service;

import hotsixturtles.tupli.dto.PlaylistDto;
import hotsixturtles.tupli.dto.response.ResponsePlayroomDto;
import hotsixturtles.tupli.dto.simple.SimpleBoardDto;
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
               infoResult.add(new PlaylistDto(playlistRepository.findById(nowHomeInfo.getInfoId()).orElse(null)));
           }
           else if(type.equals("playroom")){
               infoResult.add(new ResponsePlayroomDto(playroomRepository.findById(nowHomeInfo.getInfoId()).orElse(null)));
           }
           else{
               infoResult.add(new SimpleBoardDto(boardRepository.findById(nowHomeInfo.getInfoId()).orElse(null)));
           }
        }

        return infoResult;

    }



}
