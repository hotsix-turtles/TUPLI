package hotsixturtles.tupli.dto.simple.home;

import hotsixturtles.tupli.dto.PlaylistDto;
import hotsixturtles.tupli.dto.PlayroomDto;
import hotsixturtles.tupli.dto.simple.SimpleBadgeDto;
import hotsixturtles.tupli.dto.simple.SimpleUserDto;
import hotsixturtles.tupli.entity.Board;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class SimpleHomeBoardDto {

    private String type = "board";
    private Long id;
    private String title;
    private String content;
    private String contentType; // null, "playlist", "playroom"

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    // 유저 정보

    private Long userId;
    private String nickName;
    private String userProfileImg;
    private SimpleUserDto user;
    private Object contents;
//    private PlaylistDto playlist;
//    private PlayroomDto playroom;

//    private List<Comment> comments;
//    private List<BoardLikes> boardLikes;

    // 제작 변수
    private Integer likes_count;

    public SimpleHomeBoardDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.createdAt = board.getCreated();
        this.updatedAt = board.getUpdated();
        this.contentType = board.getType();

        // 유저
        this.userId = board.getUser().getUserSeq();
        this.nickName = board.getUser().getNickname();
        this.userProfileImg = board.getUser().getProfileImage();
        // 연결
        if(this.contentType.equals("playroom")){
            this.contents = new SimpleHomePlayroomDto(board.getPlayroom());
        }
        else if(this.contentType.equals("playlist")){
            this.contents = new SimpleHomePlaylistDto(board.getPlaylist());
        }

        // 변수
        this.likes_count = board.getBoardLikes() == null ? 0 : board.getBoardLikes().size();

    }

}
