package hotsixturtles.tupli.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hotsixturtles.tupli.entity.auth.ProviderType;
import hotsixturtles.tupli.entity.auth.RoleType;
import hotsixturtles.tupli.entity.likes.BoardLikes;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "USER")
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    // OAUTH 관련 column
    @JsonIgnore
    @Id
    @Column(name = "USER_SEQ")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSeq;

    @Column(name = "USER_ID", length = 64, unique = true)
//    @NotNull
    @Size(max = 64)
    private String userId;

    @Column(name = "EMAIL_VERIFIED_YN", length = 1)
//    @NotNull
    @Size(min = 1, max = 1)
    private String emailVerifiedYn;

    @Column(name = "PROVIDER_TYPE", length = 20)
    @Enumerated(EnumType.STRING)
//    @NotNull
    private ProviderType providerType;  // 구글, 카카오, 네이버

    @Column(name = "ROLE_TYPE", length = 20)
    @Enumerated(EnumType.STRING)
//    @NotNull
    private RoleType roleType;

    @Column(name = "CREATED_AT")
//    @NotNull
    private LocalDateTime createdAt;

    @Column(name = "MODIFIED_AT")
//    @NotNull
    private LocalDateTime modifiedAt;

    @Column(name = "USERNAME", length = 100)
//    @NotNull
    @Size(max = 100)
    private String username;

    @Size(max=150)
    private String nickname;

    @JsonIgnore
    @Column(name = "PASSWORD", length = 128)
    @NotNull
    @Size(max = 128)
    private String password;

    @Column(name = "EMAIL", length = 512, unique = true)
    @NotNull
    @Size(max = 512)
    private String email;

    @Column(name = "IS_VIP", length = 1)
//    @NotNull
    @Size(min = 1, max = 1)
    private String is_vip;

    @Column(name = "PROFILE_IMAGE_URL", length = 512)
//    @NotNull
    @Size(max = 512)
    private String profileImageUrl;

    private String introduction = "안녕하세요. 잘 부탁드리겠습니다";

    private String authKey;

    // 연결
    @OneToMany(mappedBy = "user")
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<BoardLikes> boardLikes = new ArrayList<>();

    /**
     * Security 회원 가입
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User(
            @Size(max = 64) String userId,
            @NotNull @Size(max = 100) String username,
            @NotNull @Size(max = 512) String email,
            @Size(max = 1) String emailVerifiedYn,
            @Size(max = 512) String profileImageUrl,
            ProviderType providerType,
            RoleType roleType,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt,
            String authKey
    ) {
        this.userId = userId;
        this.username = username;
        this.password = "NO_PASS";
        this.email = email != null ? email : "NO_EMAIL";
        this.emailVerifiedYn = emailVerifiedYn != null ? emailVerifiedYn :"N";
        this.profileImageUrl = profileImageUrl != null ? profileImageUrl : "";
        this.providerType = providerType != null ? providerType : ProviderType.LOCAL;
        this.roleType = roleType != null ? roleType : RoleType.USER;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.authKey = authKey;
    }

    @Transactional
    public void encodePassword(PasswordEncoder passwordEncoder){
        password = passwordEncoder.encode(password);
    }
}

