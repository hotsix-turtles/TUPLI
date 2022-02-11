package hotsixturtles.tupli.service;

import hotsixturtles.tupli.dto.simple.SimpleHomeInfoDto;
import hotsixturtles.tupli.entity.Board;
import hotsixturtles.tupli.entity.HomeInfo;
import hotsixturtles.tupli.entity.likes.BoardLikes;
import hotsixturtles.tupli.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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

    private final BoardLikesRepository boardLikesRepository;

    private final BoardRepositoryCustom boardRepositoryCustom;

    private final HomeInfoRepository homeInfoRepository;

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

    public List<Board> getHomeBoardList(Pageable pageable){
        return boardRepositoryCustom.listByHomeBoard(pageable);
    }

    public Board getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElse(null);
        return board;
    }

    public List<Board> getLikedBoards(Long userSeq){
        return boardRepository.findLikedBoards(userSeq);
    }

    @Transactional
    public Board addBoard(Long userSeq, Board board){

        board.setUser(userRepository.findByUserSeq(userSeq));
        Board nowBoard = boardRepository.save(board);

        HomeInfo homeInfo = new HomeInfo();
        homeInfo.setType("board");
        homeInfo.setInfoId(nowBoard.getId());

        homeInfoRepository.save(homeInfo);

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
        homeInfoRepository.deleteByInfoId(boardId);
    }

    public BoardLikes getBoardLike(Long userSeq, Long boardId){
        return boardLikesRepository.findExist(userSeq, boardId);
    }

    @Transactional
    public void addBoardLike(Long userSeq, Long boardId) {

        BoardLikes existBoardLikes = boardLikesRepository.findExist(userSeq, boardId);
        if(existBoardLikes == null) {
            BoardLikes boardLikes = new BoardLikes();
            boardLikes.setBoard(boardRepository.findById(boardId).orElse(null));
            boardLikes.setUser(userRepository.findByUserSeq(userSeq));
            boardLikesRepository.save(boardLikes);
        } else {
            // 익셉션 발생
        }
    }

    @Transactional
    public void deleteBoardLike(Long userSeq, Long boardId) {

        BoardLikes existBoardLikes = boardLikesRepository.findExist(userSeq, boardId);
        if(existBoardLikes != null) {
            boardLikesRepository.delete(existBoardLikes);
        } else {
            // 익셉션 발생
        }
    }


}
