package hotsixturtles.tupli.service;

import hotsixturtles.tupli.dto.simple.home.SimpleHomeBoardDto;
import hotsixturtles.tupli.dto.simple.home.SimpleHomePlaylistDto;
import hotsixturtles.tupli.dto.simple.home.SimpleHomePlayroomDto;
import hotsixturtles.tupli.entity.HomeInfo;
import hotsixturtles.tupli.entity.Playlist;
import hotsixturtles.tupli.entity.Playroom;
import hotsixturtles.tupli.repository.BoardRepository;
import hotsixturtles.tupli.repository.HomeInfoRepository;
import hotsixturtles.tupli.repository.PlaylistRepository;
import hotsixturtles.tupli.repository.PlayroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class HomeInfoService {

    private final HomeInfoRepository homeInfoRepository;
    private final PlaylistRepository playlistRepository;

    private final PlayroomRepository playroomRepository;

    private final BoardRepository boardRepository;

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
    public List<Object> getRecommendHomeList(List<Object> homeInfos, ConcurrentHashMap<String, Integer> tastes, Pageable pageable){
        List<String> userTaste = new ArrayList<>();
        for(Map.Entry<String,Integer> tasteMap : tastes.entrySet()){
            userTaste.add(tasteMap.getKey());
        }

        Page<Playlist> playlists = playlistRepository.findAll(pageable);
        List<Playlist> plList = playlists.getContent();

        for(Playlist nowPlaylist : plList){
            if(nowPlaylist.getPlaylistCate() == null || nowPlaylist.getPlaylistCate().trim().length() == 0){
                continue;
            }
            for(String nowPlCate : nowPlaylist.getPlaylistCate().split(",")){
                if(userTaste.contains(nowPlCate)){
                    SimpleHomePlaylistDto nowDto = new SimpleHomePlaylistDto(nowPlaylist);
                    if(!homeInfos.contains(nowDto)) homeInfos.add(nowDto);
                    break;
                }
            }
        }

        Pageable sortedByTime = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("startTime").descending());

        Page<Playroom> playrooms = playroomRepository.findAll(sortedByTime);
        List<Playroom> playroomList = playrooms.getContent();

        for(Playroom nowPlayroom : playroomList){
            if(nowPlayroom.getPlayroomCate() == null || nowPlayroom.getPlayroomCate().trim().length() == 0){
                continue;
            }
            for(String nowPlayroomCate : nowPlayroom.getPlayroomCate().split(",")){
                if(userTaste.contains(nowPlayroomCate)){
                    SimpleHomePlayroomDto nowDto = new SimpleHomePlayroomDto(nowPlayroom);
                    if(!homeInfos.contains(nowDto)) homeInfos.add(nowDto);
                    break;
                }
            }
        }

        return homeInfos;

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
