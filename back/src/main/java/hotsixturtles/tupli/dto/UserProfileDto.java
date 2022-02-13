package hotsixturtles.tupli.dto;

import hotsixturtles.tupli.dto.simple.SimpleUserDto;
import hotsixturtles.tupli.entity.User;
import hotsixturtles.tupli.entity.auth.RoleType;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@RequiredArgsConstructor
public class UserProfileDto {

    private Long userSeq;
    //    private String userId;
//    private String emailVerifiedYn;
//    private ProviderType providerType;
    private RoleType roleType;
    private LocalDateTime createdAt;
    //    private LocalDateTime modifiedAt;
    private String username;
    private String nickname;
    //    private String password;
    private String email;
    private String profileImage;
    private String introduction;
    private String is_vip;
    private List<String> taste;


    // 연결
//    private List<Board> boards = new ArrayList<>();
//    private List<Comment> comments = new ArrayList<>();
    private List<SimpleUserDto> from_user;
    private List<SimpleUserDto> to_user;
//    private List<BoardLikes> boardLikes = new ArrayList<>();
//    private List<CommentLikes> commentLikes = new ArrayList<>();
//    private List<Playlist> playlists = new ArrayList<>();

    // 추가 변수
    private Integer boards_count;


    public UserProfileDto(User user) {
        this.userSeq = user.getUserSeq();
        this.roleType = user.getRoleType();
        this.createdAt = user.getCreatedAt();
        this.username = user.getUsername();
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
        this.from_user = user.getFrom_user().stream().map(u -> new SimpleUserDto(u.getFromUser())).collect(toList());
        this.to_user = user.getTo_user().stream().map(u-> new SimpleUserDto(u.getToUser())).collect(toList());

        // 추가 변수
        this.boards_count = user.getBoards() == null ? 0 : user.getBoards().size();
    }
}
