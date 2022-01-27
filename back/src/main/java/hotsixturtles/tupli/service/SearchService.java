package hotsixturtles.tupli.service;

import hotsixturtles.tupli.dto.params.BoardSearchCondition;
import hotsixturtles.tupli.dto.params.UserSearchCondition;
import hotsixturtles.tupli.entity.Board;
import hotsixturtles.tupli.entity.User;
import hotsixturtles.tupli.repository.BoardRepositoryImpl;
import hotsixturtles.tupli.repository.UserRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final UserRepositoryImpl searchRepository;

    private final BoardRepositoryImpl boardRepository;

    public List<User> searchUser(UserSearchCondition userSearchCondition, Pageable pageable){
        return searchRepository.searchByPageSimpleUser(userSearchCondition, pageable);
    }
    public List<Board> searchBoard(BoardSearchCondition boardSearchCondition, Pageable pageable){
        return boardRepository.searchByPageSimpleBoard(boardSearchCondition, pageable);
    }
}
