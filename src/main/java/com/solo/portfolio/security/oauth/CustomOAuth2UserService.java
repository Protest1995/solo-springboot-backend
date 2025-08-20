package com.solo.portfolio.security.oauth;

import com.solo.portfolio.model.entity.User;
import com.solo.portfolio.model.entity.UserRole;
import com.solo.portfolio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * 自定義OAuth2使用者服務
 * 處理第三方登入（如Google、Facebook等）的使用者資訊載入和處理
 * 實現自動註冊和使用者資料同步功能
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String email = getAttribute(attributes, "email");
        String name = getAttribute(attributes, "name");
        String picture = null;
        String username = null;

        if ("google".equals(registrationId)) {
            picture = getAttribute(attributes, "picture");
        } else if ("facebook".equals(registrationId)) {
            if (attributes.containsKey("picture")) {
                Map<String, Object> pictureObj = (Map<String, Object>) attributes.get("picture");
                if (pictureObj.containsKey("data")) {
                    Map<String, Object> dataObj = (Map<String, Object>) pictureObj.get("data");
                    if (dataObj.containsKey("url")) {
                        picture = (String) dataObj.get("url");
                    }
                }
            }
        } else if ("github".equals(registrationId)) {
            // GitHub: login is username, name is display name, email may be null
            username = getAttribute(attributes, "login");
            if (email == null || email.isBlank()) {
                // Try to get primary email from emails attribute if available (Spring may not fetch it by default)
                // Fallback: use login@github.oauth
                email = (username != null ? username : "githubUser") + "@github.oauth";
            }
            if (name == null || name.isBlank()) {
                name = username;
            }
        }

        log.info("OAuth2登入來源 {}: 電子郵件={}, 姓名={}, 用戶名={}", registrationId, email, name, username);

        ensureUserExists(email, name, picture != null ? picture : null);
        log.info("OAuth2 ensureUserExists finished for email={}.", email);

        return new DefaultOAuth2User(
                oAuth2User.getAuthorities(),
                attributes,
                userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName()
        );
    }

    private void ensureUserExists(String email, String name, String avatarUrl) {
        Optional<User> existingByEmail = userRepository.findByEmail(email);
        if (existingByEmail.isPresent()) return;

        String baseUsername = email.contains("@") ? email.substring(0, email.indexOf('@')) : email;
        String candidate = baseUsername;
        int suffix = 1;
        while (userRepository.existsByUsername(candidate)) {
            candidate = baseUsername + suffix;
            suffix++;
        }

        User user = new User();
        user.setUsername(candidate);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
        user.setRole(UserRole.USER);
        user.setAvatarUrl(avatarUrl);
        userRepository.save(user);
        log.info("Created new OAuth2 user: {} ({})", candidate, email);
    }

    @SuppressWarnings("unchecked")
    private static String getAttribute(Map<String, Object> attributes, String key) {
        Object value = attributes.get(key);
        return value == null ? null : String.valueOf(value);
    }
}


