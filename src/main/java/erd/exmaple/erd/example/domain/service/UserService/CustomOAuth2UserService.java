package erd.exmaple.erd.example.domain.service.UserService;

import erd.exmaple.erd.example.domain.UserEntity;
import erd.exmaple.erd.example.domain.enums.Provider;
import erd.exmaple.erd.example.domain.jwt.JwtUtil;
import erd.exmaple.erd.example.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 사용자 정보를 처리
        String providerId = oAuth2User.getName();
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        if (name == null || name.isEmpty()) {
            name = "DefaultNickname"; // 기본 닉네임 설정
        }

        Optional<UserEntity> userEntityOptional = userRepository.findByProviderId(providerId);

        UserEntity userEntity;
        if (userEntityOptional.isPresent()) {
            userEntity = userEntityOptional.get();
        } else {
            userEntity = new UserEntity();
            userEntity.setProviderId(providerId);
            userEntity.setProvider(Provider.valueOf(provider.toUpperCase()));
            userEntity.setNickname(name);
            userEntity.setEmail(email);
            userEntity.setPassword("defaultPassword");
            userEntity.setPhoneNumber(generateRandomPhoneNumber());
            userRepository.save(userEntity);
        }

        // JWT 토큰 생성
        String jwtToken = jwtUtil.generateToken(userEntity.getPhoneNumber());

        // JWT 토큰을 OAuth2User에 추가하여 반환
        return new CustomOAuth2User(oAuth2User, jwtToken);
    }

    private String generateRandomPhoneNumber() {
        Random random = new Random();
        StringBuilder phoneNumber = new StringBuilder("101");
        for (int i = 0; i < 8; i++) {
            phoneNumber.append(random.nextInt(10));
        }
        return phoneNumber.toString();
    }
}
