package hotsixturtles.tupli.service;

import hotsixturtles.tupli.entity.PlaylistComment;
import hotsixturtles.tupli.entity.likes.BoardLikes;
import hotsixturtles.tupli.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlaylistCommentService {

    private final PlaylistCommentRepository playlistCommentRepository;

    private final PlaylistRepository playlistRepository;

    private final UserRepository userRepository;

    public List<PlaylistComment> getCommentList(Long playlistId){
        return playlistCommentRepository.findAllByPlaylistId(playlistId);
    }

    @Transactional
    public PlaylistComment addComment(Long userSeq, Long playlistId, PlaylistComment playlistComment){

        playlistComment.setUser(userRepository.findByUserSeq(userSeq));
        playlistComment.setPlaylist(playlistRepository.findById(playlistId).orElse(null));
        playlistCommentRepository.save(playlistComment);
        return playlistComment;

    }

    @Transactional
    public PlaylistComment updateComment(Long userSeq, Long playlistId, PlaylistComment playlistComment){
        PlaylistComment nowPlaylistComment = playlistCommentRepository
                .findById(playlistId).orElse(null);
        Long commentUserSeq = nowPlaylistComment.getUser().getUserSeq();

        if(userSeq == commentUserSeq){
            nowPlaylistComment.setContent(playlistComment.getContent());
            playlistCommentRepository.save(nowPlaylistComment);
        }
        else{
            return null;
        }
        return nowPlaylistComment;
    }

    @Transactional
    public Long deleteComment(Long commentId, Long userSeq){
        PlaylistComment nowComment = playlistCommentRepository.findById(commentId).orElse(null);
        Long commentUserSeq = nowComment.getUser().getUserSeq();
        if(userSeq == commentUserSeq){
            playlistCommentRepository.deleteById(commentId);
            return commentId;
        }
        return -1L;
    }

}
