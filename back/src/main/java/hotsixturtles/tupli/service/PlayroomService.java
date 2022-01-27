package hotsixturtles.tupli.service;

import hotsixturtles.tupli.dto.PlayroomDto;
import hotsixturtles.tupli.dto.simple.SimpleUserDto;
import hotsixturtles.tupli.entity.Playroom;
import hotsixturtles.tupli.entity.User;
import hotsixturtles.tupli.repository.PlayroomRepository;
import hotsixturtles.tupli.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayroomService {

    private final UserRepository userRepository;

    private final PlayroomRepository playroomRepository;

    public List<Playroom> getPlayroomList(){

        // Paging 조건 정해서 추가
        return playroomRepository.findAll();
    }

    public Playroom getPlayroom(Long playroomId){

        // Paging 조건 정해서 추가
        return playroomRepository.findById(playroomId).orElse(null);
    }

    public PlayroomDto addPlayroom(PlayroomDto playroomDto, Long userSeq){

        User owner = userRepository.findByUserSeq(userSeq);

        playroomDto.setUser(new SimpleUserDto(owner));

        Playroom playroom = new Playroom(playroomDto, owner);

        playroomRepository.save(playroom);

        return new PlayroomDto(playroom);

    }
}
