package erd.exmaple.erd.example.domain.service.UserService;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import erd.exmaple.erd.example.domain.UserEntity;
import erd.exmaple.erd.example.domain.dto.UserDTO;
import erd.exmaple.erd.example.domain.enums.Provider;
import erd.exmaple.erd.example.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceSocial {

    @Autowired
    private final UserRepository userRepository;

    public UserDTO processOAuth2User(OAuth2User oAuth2User) {
        String providerId = oAuth2User.getName();
        Optional<UserEntity> userEntityOptional = userRepository.findByProviderId(providerId);

        UserEntity userEntity;
        if (userEntityOptional.isPresent()) {
            userEntity = userEntityOptional.get();
        } else {
            // 새로운 사용자 생성
            userEntity = new UserEntity();
            userEntity.setProviderId(providerId);
            userEntity.setNickname(oAuth2User.getAttribute("name"));
            userEntity.setProvider(Provider.valueOf(oAuth2User.getAttribute("provider")));
            userRepository.save(userEntity);
        }

        return UserDTO.toUserDTO(userEntity);
    }


    @Value("a090ed16ad7cf9039104c87a03989c38") //client id kakao
    private String restApiKey;

    public Long getKakaoIdToken(String accessToken) throws Exception {
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        return getIdTokenFromProvider(accessToken, reqURL, "id", restApiKey);
    }

    public Long getNaverIdToken(String accessToken) throws Exception {
        String reqURL = "https://openapi.naver.com/v1/nid/me";
        return getIdTokenFromProvider(accessToken, reqURL, "response.id", null);
    }

    public Long getGoogleIdToken(String accessToken) throws Exception {
        String reqURL = "https://www.googleapis.com/oauth2/v3/userinfo";
        return getIdTokenFromProvider(accessToken, reqURL, "sub", null);
    }

    private Long getIdTokenFromProvider(String accessToken, String reqURL, String idKey, String restApiKey) throws Exception {
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            if (restApiKey != null && !restApiKey.isEmpty()) {
                conn.setRequestProperty("Authorization", "KakaoAK " + restApiKey);
            }

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                result.append(line);
            }

            System.out.println("response body : " + result);

            JsonElement element = JsonParser.parseString(result.toString());

            long idToken = element.getAsJsonObject().get(idKey).getAsLong();
            System.out.println("id : " + idToken);

            br.close();
            return idToken;
        } catch (IOException e) {
            throw new Exception("서버에서 사용자 정보를 가져오는 중에 문제가 발생했습니다.", e);
        }
    }

    public UserDTO findUserById(Long id) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
        return optionalUserEntity.map(UserDTO::toUserDTO).orElse(null);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}