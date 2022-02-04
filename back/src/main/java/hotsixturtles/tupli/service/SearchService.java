package hotsixturtles.tupli.service;

import hotsixturtles.tupli.dto.params.BoardSearchCondition;
import hotsixturtles.tupli.dto.params.UserSearchCondition;
import hotsixturtles.tupli.entity.Board;
import hotsixturtles.tupli.entity.User;
import hotsixturtles.tupli.repository.BoardRepositoryImpl;
import hotsixturtles.tupli.dto.params.PlayroomSearchCondition;
import hotsixturtles.tupli.dto.params.UserSearchCondition;
import hotsixturtles.tupli.entity.Playroom;
import hotsixturtles.tupli.entity.User;
import hotsixturtles.tupli.repository.PlayroomRepositoryImpl;
import hotsixturtles.tupli.repository.UserRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final UserRepositoryImpl searchUserRepository;

    private final PlayroomRepositoryImpl searchPlayroomRepository;

    private final BoardRepositoryImpl boardRepository;

    public List<User> searchUser(UserSearchCondition userSearchCondition, Pageable pageable){
        return searchUserRepository.searchByPageSimpleUser(userSearchCondition, pageable);
    }
    public List<Playroom> searchPlayroom(PlayroomSearchCondition playroomSearchCondition, Pageable pageable){
        return searchPlayroomRepository.searchByPageSimplePlayroom(playroomSearchCondition, pageable);
    }
    public List<Board> searchBoard(BoardSearchCondition boardSearchCondition, Pageable pageable){
        return boardRepository.searchByPageSimpleBoard(boardSearchCondition, pageable);
    }
}
