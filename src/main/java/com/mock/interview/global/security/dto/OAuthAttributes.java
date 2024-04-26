package com.mock.interview.global.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@ToString
public class OAuthAttributes {
    private String email;
    private String username;
    private String picture;
    private Map<String, Object> attributes;
    private String nameAttributeKey;

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if(registrationId.equals("google"))
            return ofGoogle(userNameAttributeName, attributes);
        else
            throw new IllegalArgumentException("지원하지 않는 OAuth 플랫폼");
    }

    private static OAuthAttributes ofGoogle(String usernameAttributeName, Map<String, Object> attributes) {
        String email = (String) attributes.get("email");
        String username = email.split("@")[0];
        String picture = (String) attributes.get("picture");
        return OAuthAttributes.builder()
                .username(username)
                .email(email)
                .picture(picture)
                .attributes(attributes)
                .nameAttributeKey(usernameAttributeName)
                .build();
    }
}
