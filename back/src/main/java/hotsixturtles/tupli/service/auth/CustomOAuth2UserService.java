package hotsixturtles.tupli.service.auth;

import hotsixturtles.tupli.entity.User;
import hotsixturtles.tupli.entity.UserBadge;
import hotsixturtles.tupli.entity.auth.ProviderType;
import hotsixturtles.tupli.entity.auth.RoleType;
import hotsixturtles.tupli.entity.auth.UserPrincipal;
import hotsixturtles.tupli.entity.meta.UserInfo;
import hotsixturtles.tupli.exception.OAuthProviderMissMatchException;
import hotsixturtles.tupli.info.OAuth2UserInfo;
import hotsixturtles.tupli.info.OAuth2UserInfoFactory;
import hotsixturtles.tupli.repository.UserInfoRepository;
import hotsixturtles.tupli.repository.UserRepository;

import hotsixturtles.tupli.service.BadgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    private final UserInfoRepository userInfoRepository;

    private final BadgeService badgeService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);

        try {
            return this.process(userRequest, user);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user) {
        ProviderType providerType = ProviderType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());
        User savedUser = userRepository.findByUserId(userInfo.getId());

        if (savedUser != null) {
            if (providerType != savedUser.getProviderType()) {
                throw new OAuthProviderMissMatchException(
                        "Looks like you're signed up with " + providerType +
                                " account. Please use your " + savedUser.getProviderType() + " account to login."
                );
            }
            updateUser(savedUser, userInfo);
            UserInfo nowUserInfo =  userInfoRepository.findOneByUserSeq(savedUser.getUserSeq());
            nowUserInfo.setLoginCount(nowUserInfo.getLoginCount() + 1L);

            List<UserBadge> userbadges = badgeService.getBadgeList(savedUser.getUserSeq());
            List<Long> badges = badgeService.getUserBadgeSeq(userbadges);

            badgeService.checkLoginNum(savedUser.getUserSeq(), badges);

            if(nowUserInfo.getDailyLoginYN().equals("N")) {
                nowUserInfo.setDailyLoginYN("Y");
                nowUserInfo.setDailyCheck(nowUserInfo.getDailyCheck() + 1L);
                badgeService.checkDaily(savedUser.getUserSeq(), badges);
            }

            userInfoRepository.save(nowUserInfo);
        } else {
            savedUser = createUser(userInfo, providerType);
            UserInfo nowUserInfo = new UserInfo(null, savedUser.getUserSeq(), null, 0L, 0L, 1L, 1L, "Y");
            userInfoRepository.save(nowUserInfo);
        }

        return UserPrincipal.create(savedUser, user.getAttributes());
    }

    private User createUser(OAuth2UserInfo userInfo, ProviderType providerType) {
        LocalDateTime now = LocalDateTime.now();
        User user = new User(
                userInfo.getId(),
                userInfo.getName(),
                userInfo.getEmail(),
                "Y",
                userInfo.getImageUrl(),
                providerType,
                RoleType.USER,
                now,
                now,
                null
        );

        return userRepository.saveAndFlush(user);
    }

    private User updateUser(User user, OAuth2UserInfo userInfo) {
        if (userInfo.getName() != null && !user.getUsername().equals(userInfo.getName())) {
            user.setUsername(userInfo.getName());
        }

        if (userInfo.getImageUrl() != null && !user.getProfileImage().equals(userInfo.getImageUrl())) {
            user.setProfileImage(userInfo.getImageUrl());
        }

        return user;
    }
}
