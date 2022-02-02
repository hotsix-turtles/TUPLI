package hotsixturtles.tupli.service;

import hotsixturtles.tupli.entity.Comment;
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
public class CommentService {

    private final CommentRepository commentRepository;

    private final BoardRepository boardRepository;

    private final UserRepository userRepository;

    public List<Comment> getCommentList(Long boardId){
        return commentRepository.findAllByBoardId(boardId);
    }

    @Transactional
    public Comment addComment(Long userSeq, Long boardId, Comment comment){

        comment.setUser(userRepository.findByUserSeq(userSeq));
        comment.setBoard(boardRepository.findById(boardId).orElse(null));
        commentRepository.save(comment);
        return comment;

    }

    @Transactional
    public Comment updateComment(Long userSeq, Long boardId, Comment comment){
        Comment nowComment = commentRepository.findById(comment.getId()).orElse(null);
        Long commentUserSeq = nowComment.getUser().getUserSeq();

        if(userSeq == commentUserSeq){
            nowComment.setContent(comment.getContent());
            commentRepository.save(nowComment);
        }
        else{
            return null;
        }
        return nowComment;
    }

    @Transactional
    public Long deleteComment(Long commentId, Long userSeq){
        Comment nowComment = commentRepository.findById(commentId).orElse(null);
        Long commentUserSeq = nowComment.getUser().getUserSeq();
        if(userSeq == commentUserSeq){
            commentRepository.deleteById(commentId);
            return commentId;
        }
        return -1L;
    }
}
