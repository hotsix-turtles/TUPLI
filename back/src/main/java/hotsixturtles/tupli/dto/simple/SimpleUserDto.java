package hotsixturtles.tupli.dto.simple;

import hotsixturtles.tupli.entity.User;
import hotsixturtles.tupli.entity.auth.ProviderType;
import hotsixturtles.tupli.entity.auth.RoleType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 유저 관련 DTO
 */
@Data
public class SimpleUserDto {

    private Long userSeq;
    //private String userId;
    private String emailVerifiedYn;
    private ProviderType providerType;
    private RoleType roleType;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String username;
    private String nickname;
    //private String password;
    private String email;
    private String profileImage;
    private String introduction;
    private String authKey;
    private String is_vip;

    public SimpleUserDto(User user) {
        this.userSeq = user.getUserSeq();
        //this.userId = userId;
        this.emailVerifiedYn = user.getEmailVerifiedYn();
        this.providerType = user.getProviderType();
        this.roleType = user.getRoleType();
        this.createdAt = user.getCreatedAt();
        this.modifiedAt = user.getModifiedAt();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        //this.password = password;
        this.email = user.getEmail();
        if (user.getProfileImage() == null || user.getProfileImage().equals("")) {
            this.profileImage = null;
        } else {
            this.profileImage = user.getProfileImage();
        }
        this.introduction = user.getIntroduction();
        this.authKey = user.getAuthKey();
        this.is_vip = user.getIs_vip();
    }

}
