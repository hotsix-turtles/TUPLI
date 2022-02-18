package hotsixturtles.tupli.dto.response;

import hotsixturtles.tupli.dto.PlaylistDto;
import hotsixturtles.tupli.dto.PlayroomDto;
import hotsixturtles.tupli.dto.simple.SimpleBadgeDto;
import hotsixturtles.tupli.dto.simple.SimplePlaylistDto;
import hotsixturtles.tupli.dto.simple.SimpleUserDto;
import hotsixturtles.tupli.entity.Board;
import hotsixturtles.tupli.entity.Playlist;
import hotsixturtles.tupli.entity.User;
import hotsixturtles.tupli.entity.likes.BoardLikes;
import hotsixturtles.tupli.entity.likes.PlaylistLikes;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class BoardResponseDto {

    private Long id;
    private String title;
    private String content;
    private String type; // null, "playlist", "playroom"
    private String image;

    private OffsetDateTime created;
    private OffsetDateTime updated;
    private Boolean isLiked;

    // 연결
    private SimpleUserDto user;
    private PlaylistDto playlist;
    private PlayroomDto playroom;
//    private List<Comment> comments;
//    private List<BoardLikes> boardLikes;

    // 제작 변수
    private Integer likes_count;

    private List<SimpleBadgeDto> badges;

    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.type = board.getType();
        this.created = board.getCreated();
        this.updated = board.getUpdated();

        // 연결
        this.user = new SimpleUserDto(board.getUser());
        if (board.getPlaylist() != null) {
            this.playlist = new PlaylistDto(board.getPlaylist());
            this.image = board.getPlaylist().getImage();
        }
        if (board.getPlayroom() != null) {
            this.playroom = new PlayroomDto(board.getPlayroom());
            this.image = board.getPlayroom().getImage();
        }

        // 변수
        this.likes_count = board.getBoardLikes() == null ? 0 : board.getBoardLikes().size();

    }

    public BoardResponseDto(Board board, User user) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.type = board.getType();
        this.created = board.getCreated();
        this.updated = board.getUpdated();

        // 연결
        this.user = new SimpleUserDto(board.getUser());
        if (board.getPlaylist() != null) {
            this.playlist = new PlaylistDto(board.getPlaylist());
            this.image = board.getPlaylist().getImage();
        }
        if (board.getPlayroom() != null) {
            this.playroom = new PlayroomDto(board.getPlayroom());
            this.image = board.getPlayroom().getImage();
        }

        // 변수
        this.likes_count = board.getBoardLikes() == null ? 0 : board.getBoardLikes().size();

        if(board.getBoardLikes() == null){
            this.isLiked = false;
        }
        else{
            this.isLiked = false;
            for(BoardLikes nowBoardLikes : board.getBoardLikes()){
                if(nowBoardLikes.getUser().getUserSeq() == user.getUserSeq()){
                    this.isLiked = true;
                    break;
                }
            }
        }
    }

//
//    public BoardResponseDto(Board board, List<SimpleBadgeDto> simpleBadgeDtoList) {
//        this.id = board.getId();
//        this.title = board.getTitle();
//        this.content = board.getContent();
//        this.type = board.getType();
//        this.created = board.getCreated();
//        this.updated = board.getUpdated();
//
//        // 연결
//        this.user = new SimpleUserDto(board.getUser());
//        if (board.getPlaylist() != null) {
//            this.playlist = new PlaylistDto(board.getPlaylist());
//            this.image = board.getPlaylist().getImage();
//        }
//        if (board.getPlayroom() != null) {
//            this.playroom = new PlayroomDto(board.getPlayroom());
//            this.image = board.getPlayroom().getImage();
//        }
//
//        // 변수
//        this.likes_count = board.getBoardLikes() == null ? 0 : board.getBoardLikes().size();
//
//        this.badges = simpleBadgeDtoList;
//    }

    public BoardResponseDto(Board board, List<SimpleBadgeDto> simpleBadgeDtoList, User user) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.type = board.getType();
        this.created = board.getCreated();
        this.updated = board.getUpdated();

        // 연결
        this.user = new SimpleUserDto(board.getUser());
        if (board.getPlaylist() != null) {
            this.playlist = new PlaylistDto(board.getPlaylist());
            this.image = board.getPlaylist().getImage();
        }
        if (board.getPlayroom() != null) {
            this.playroom = new PlayroomDto(board.getPlayroom());
            this.image = board.getPlayroom().getImage();
        }

        // 변수
        this.likes_count = board.getBoardLikes() == null ? 0 : board.getBoardLikes().size();

        this.badges = simpleBadgeDtoList;

        if(board.getBoardLikes() == null){
            this.isLiked = false;
        }
        else{
            this.isLiked = false;
            for(BoardLikes nowBoardLikes : board.getBoardLikes()){
                if(nowBoardLikes.getUser().getUserSeq() == user.getUserSeq()){
                    this.isLiked = true;
                    break;
                }
            }
        }
    }
}
