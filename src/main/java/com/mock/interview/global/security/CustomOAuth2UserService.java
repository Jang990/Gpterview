package com.mock.interview.global.security;

import com.mock.interview.global.security.dto.LoginUser;
import com.mock.interview.global.security.dto.OAuthAttributes;
import com.mock.interview.user.domain.model.Users;
import com.mock.interview.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> defaultOAuthService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = defaultOAuthService.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // 네이버, 구글 등 서비스 구분을 위한 값. - 구글은 "google"
        String usernameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName(); // 해당 서비스에서 사용하는 유니크 ID명 - [구글은 sub | 네이버는 id] 숫자값 등등 ID 값이 들어간다.

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, usernameAttributeName, oAuth2User.getAttributes());

        Users users = saveOrUpdate(attributes);
        return new LoginUser(users.getId(), users.getEmail(), users.getUsername(), users.getRole());
    }

    private Users saveOrUpdate(OAuthAttributes attributes) {
        Users user = userRepository.findByEmail(attributes.getEmail())
                .orElseGet(() -> Users.createUser(
                        attributes.getEmail(), attributes.getUsername(), attributes.getPicture()
                ));

        user.updatePicture(attributes.getPicture());
        return userRepository.save(user);
    }
}
