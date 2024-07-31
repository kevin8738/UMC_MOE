package erd.exmaple.erd.example.domain.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.springframework.http.*;

@RestController
@RequestMapping("/auth")
public class SocialAuthController {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String naverRedirectUri;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String googleRedirectUri;

    @PostMapping("/kakao/callback")
    public ResponseEntity<?> kakaoCallback(@RequestParam String code) {
        return getAccessToken("https://kauth.kakao.com/oauth/token", kakaoClientId, kakaoRedirectUri, code);
    }

    @PostMapping("/naver/callback")
    public ResponseEntity<?> naverCallback(@RequestParam String code) {
        return getAccessToken("https://nid.naver.com/oauth2.0/token", naverClientId, naverRedirectUri, code);
    }

    @PostMapping("/google/callback")
    public ResponseEntity<?> googleCallback(@RequestParam String code) {
        return getAccessToken("https://oauth2.googleapis.com/token", googleClientId, googleRedirectUri, code);
    }

    private ResponseEntity<?> getAccessToken(String tokenUri, String clientId, String redirectUri, String code) {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(tokenUri, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                // 액세스 토큰 파싱
                String body = response.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(body);
                String accessToken = jsonNode.get("access_token").asText();
                return ResponseEntity.ok(accessToken);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("액세스 토큰 파싱 중 오류 발생");
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("소셜 로그인 인증 실패");
        }
    }
}