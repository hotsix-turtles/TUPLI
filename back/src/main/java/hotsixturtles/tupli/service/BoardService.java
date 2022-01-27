package hotsixturtles.tupli.service;

import hotsixturtles.tupli.entity.Board;
import hotsixturtles.tupli.repository.BoardRepository;
import hotsixturtles.tupli.repository.CommentRepository;
import hotsixturtles.tupli.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    private final UserRepository userRepository;

    private final CommentRepository commentRepository;

//    @Transactional
//    public Board boardPost(Long id, String title, String content) {
//        Board board = new Board();
//        board.setTitle(title);
//        board.setContent(content);
//        boardRepository.save(board);
//        return board;
//    }
//
    public List<Board> getBoardList(){
        return boardRepository.findAll();
    }

    public Board getBoard(Long boardId){
        Board board = boardRepository.findById(boardId).orElse(null);
        return board;
    }

    @Transactional
    public Board addBoard(Long userSeq, Board board){

        board.setUser(userRepository.findByUserSeq(userSeq));
        boardRepository.save(board);
        return board;

    }

    @Transactional
    public Board updateBoard(Long boardId, Board board){
        Board boardUpdated = boardRepository.findById(boardId).orElse(null);

        if(boardUpdated != null){
            boardUpdated.setContent(board.getContent());
            boardUpdated.setTitle(board.getTitle());
            boardRepository.save(boardUpdated);
        }
        return boardUpdated;
    }

    @Transactional
    public void deleteBoard(Long boardId){
        boardRepository.deleteById(boardId);
    }


}
