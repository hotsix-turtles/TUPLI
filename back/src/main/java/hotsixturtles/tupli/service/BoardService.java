package hotsixturtles.tupli.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hotsixturtles.tupli.dto.request.BoardRequestDto;
import hotsixturtles.tupli.dto.request.PlaylistRequest;
import hotsixturtles.tupli.dto.simple.SimpleHomeInfoDto;
import hotsixturtles.tupli.entity.*;
import hotsixturtles.tupli.entity.likes.BoardLikes;
import hotsixturtles.tupli.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static hotsixturtles.tupli.entity.QBoard.*;
import static hotsixturtles.tupli.entity.QPlaylist.playlist;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final PlaylistRepository playlistRepository;
    private final PlayroomRepository playroomRepository;
    private final BoardLikesRepository boardLikesRepository;
    private final BoardRepositoryCustom boardRepositoryCustom;
    private final HomeInfoRepository homeInfoRepository;

    // 심플 querydsl
    private final JPAQueryFactory jpaQueryFactory;

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
    public Board addBoard(Long userSeq, BoardRequestDto boardDto){
        // 기본 설정
        Board board = new Board();
        board.setTitle(boardDto.getTitle());
        board.setContent(boardDto.getContent());

        // 연결
        board.setUser(userRepository.findByUserSeq(userSeq));

        if (boardDto.getType() != null) {
            // 플레이룸 연계 게시물
            if (boardDto.getType().equals("playroom")) {
                Playroom playroom = playroomRepository.findById(boardDto.getTypeId()).orElse(null);
                if (playroom != null) {
                    board.setPlayroom(playroom);
                }
            }
            // 플레이리스트 연계 게시물
            if (boardDto.getType().equals("playlist")) {
                Playlist playlist = playlistRepository.findById(boardDto.getTypeId()).orElse(null);
                if (playlist != null) {
                    board.setPlaylist(playlist);
                }
            }
        }
        Board nowBoard = boardRepository.save(board);

        // Home쪽 연결
        HomeInfo homeInfo = new HomeInfo();
        homeInfo.setType("board");
        homeInfo.setInfoId(nowBoard.getId());
        homeInfo.setUserSeq(userSeq);
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

    public List<Board> getMyBoard(Long userSeq, Pageable pageable) {
        return jpaQueryFactory
                .select(board)
                .from(board)
                .where(board.user.userSeq.eq(userSeq))
                .orderBy(board.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
