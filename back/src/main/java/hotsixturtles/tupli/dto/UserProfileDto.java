package hotsixturtles.tupli.dto;

import hotsixturtles.tupli.dto.response.BoardResponseDto;
import hotsixturtles.tupli.dto.simple.SimpleUserDto;
import hotsixturtles.tupli.dto.simple.SimpleUserInfoDto;
import hotsixturtles.tupli.dto.simple.home.SimpleHomePlayroomDto;
import hotsixturtles.tupli.entity.Playroom;
import hotsixturtles.tupli.entity.User;
import hotsixturtles.tupli.entity.auth.RoleType;
import hotsixturtles.tupli.entity.meta.UserInfo;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Data
@RequiredArgsConstructor
public class UserProfileDto {

    private Long userSeq;

//    private RoleType roleType;
    private LocalDateTime createdAt;
    private String nickname;
    private String email;
    private String profileImage;
    private String introduction;
    private String is_vip;
    private List<String> taste;

    private List<Object> activities;

    private boolean meCheck = false;

    // 연결
//    private List<Board> boards = new ArrayList<>();
//    private List<Comment> comments = new ArrayList<>();
    private List<SimpleUserDto> from_user;
    private List<SimpleUserDto> to_user;
//    private List<BoardLikes> boardLikes = new ArrayList<>();
//    private List<CommentLikes> commentLikes = new ArrayList<>();
//    private List<Playlist> playlists = new ArrayList<>();
    private SimpleUserInfoDto userInfo;
    private List<SimpleHomePlayroomDto> watchingPlayrooms;

    // 추가 변수
//    private Integer boards_count;


    public UserProfileDto(User user) {
        this.userSeq = user.getUserSeq();
//        this.roleType = user.getRoleType();
        this.createdAt = user.getCreatedAt();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        if (user.getProfileImage() == null || user.getProfileImage().equals("")) {
            this.profileImage = null;
        } else {
            this.profileImage = user.getProfileImage();
        }
        this.introduction = user.getIntroduction();
        this.is_vip = user.getIs_vip();
        this.taste = user.getTaste();

        // 연결
        this.from_user = user.getTo_user().stream().map(u -> new SimpleUserDto(u.getFromUser())).collect(toList());
        this.to_user = user.getFrom_user().stream().map(u-> new SimpleUserDto(u.getToUser())).collect(toList());

        // 추가 변수
//        this.boards_count = user.getBoards() == null ? 0 : user.getBoards().size();
    }

    public UserProfileDto(User user, UserInfo userInfo, List<Playroom> playrooms, List<Object> activities) {
        this.userSeq = user.getUserSeq();
//        this.roleType = user.getRoleType();
        this.createdAt = user.getCreatedAt();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        if (user.getProfileImage() == null || user.getProfileImage().equals("")) {
            this.profileImage = null;
        } else {
            this.profileImage = user.getProfileImage();
        }
        this.introduction = user.getIntroduction();
        this.is_vip = user.getIs_vip();
        this.taste = user.getTaste();

        // 연결
        this.from_user = user.getTo_user().stream().map(u -> new SimpleUserDto(u.getFromUser())).collect(toList());
        this.to_user = user.getFrom_user().stream().map(u-> new SimpleUserDto(u.getToUser())).collect(toList());

        this.userInfo = new SimpleUserInfoDto(userInfo);

        if(playrooms == null || playrooms.size() == 0 ){
            this.watchingPlayrooms = null;
        }
        else {
            this.watchingPlayrooms = playrooms.stream().map(p -> new SimpleHomePlayroomDto(p)).collect(Collectors.toList());
        }
        if(activities == null || activities.size() == 0 ){
            this.activities = null;
        }
        else {
            this.activities = activities;
        }
        // 추가 변수
//        this.boards_count = user.getBoards() == null ? 0 : user.getBoards().size();
    }
}
