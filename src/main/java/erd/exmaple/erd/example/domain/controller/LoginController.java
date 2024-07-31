package erd.exmaple.erd.example.domain.controller;

import erd.exmaple.erd.example.domain.dto.ReissueTokenResponseDTO;
import erd.exmaple.erd.example.domain.dto.UserDTO;
import erd.exmaple.erd.example.domain.jwt.JwtBlacklistService;
import erd.exmaple.erd.example.domain.jwt.JwtUtil;
import erd.exmaple.erd.example.domain.service.UserService.CustomOAuth2User;
import erd.exmaple.erd.example.domain.service.UserService.UserServiceSocial;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.function.Function;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private final UserServiceSocial userServiceSocial;
    private final JwtBlacklistService jwtBlacklistService;
    private final JwtUtil jwtUtil;

    @GetMapping("/oauth2/callback/{provider}")
    public void oauth2Callback(@PathVariable String provider, OAuth2AuthenticationToken authentication, HttpServletResponse response) throws IOException {
        if (authentication == null) {
            response.sendRedirect("/login?error");
            return;
        }

        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        UserDTO userDTO = userServiceSocial.processOAuth2User((OAuth2User) customOAuth2User);

        // JWT 토큰을 응답에 포함
        String jwtToken = customOAuth2User.getJwtToken();

        // 토큰을 URL 파라미터로 리디렉션
        response.sendRedirect("/?token=" + jwtToken);
    }

    //user 경로로 접속 시 /user/login으로 리디렉션
    @GetMapping
    public String redirectToLogin() {
        log.info("Redirecting to login page");
        return "redirect:/user/login";
    }

    // 로그인 화면 (카카오, 네이버 선택)
    @GetMapping("/login")
    public String showLoginPage() {
        log.info("Accessing login page");
        return "login"; // login.html 템플릿을 반환
    }

    // 카카오 로그인 처리
    @PostMapping(value = "/kakao")
    @ResponseBody
    public ResponseEntity<?> kakaoLogin(@RequestBody ReissueTokenResponseDTO reissueTokenResponseDto) {
        return socialLogin(reissueTokenResponseDto.getAccessToken(), accessToken -> {
            try {
                return userServiceSocial.getKakaoIdToken(accessToken);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    // 네이버 로그인 처리
    @PostMapping(value = "/naver")
    @ResponseBody
    public ResponseEntity<?> naverLogin(@RequestBody ReissueTokenResponseDTO reissueTokenResponseDto) {
        return socialLogin(reissueTokenResponseDto.getAccessToken(), accessToken -> {
            try {
                return userServiceSocial.getNaverIdToken(accessToken);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    // 구글 로그인 처리
    @PostMapping(value = "/google")
    @ResponseBody
    public ResponseEntity<?> googleLogin(@RequestBody ReissueTokenResponseDTO reissueTokenResponseDto) {
        return socialLogin(reissueTokenResponseDto.getAccessToken(), accessToken -> {
            try {
                return userServiceSocial.getGoogleIdToken(accessToken);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private ResponseEntity<?> socialLogin(String accessToken, Function<String, Long> getIdTokenFunction) {
        try {
            long idToken = getIdTokenFunction.apply(accessToken);
            UserDTO userDto = userServiceSocial.findUserById(idToken);
            if (userDto != null) {
                log.info("소셜 로그인 성공 for user id: {}", userDto.getId());
                return ResponseEntity.ok(userDto);
            } else {
                log.warn("소셜 로그인 실패: user not found");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("소셜 로그인 실패: user not found");
            }
        } catch (Exception e) {
            log.error("Exception occurred while getting idToken: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("소셜 로그인 실패: internal server error");
        }
    }

    // 카카오 로그인 뷰
    @GetMapping("/kakao")
    public String showKakaoLoginPage() {
        log.info("Accessing Kakao login page");
        return "kakao-login"; // kakao-login.html 템플릿을 반환
    }

    // 네이버 로그인 뷰
    @GetMapping("/naver")
    public String showNaverLoginPage() {
        log.info("Accessing Naver login page");
        return "naver-login"; // naver-login.html 템플릿을 반환
    }

    // 구글 로그인 뷰
    @GetMapping("/google")
    public String showGoogleLoginPage() {
        log.info("Accessing Google login page");
        return "google-login"; // google-login.html 템플릿을 반환
    }

    //인증된 사용자인지 확인하는 엔드포인트
    @GetMapping("/authenticated")
    public ResponseEntity<?> checkAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return ResponseEntity.ok("User is authenticated as: " + authentication.getName());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }
    }

    private String getProviderFromJwt(String jwt) {
        // JWT 토큰에서 소셜 로그인 제공자를 추출하는 로직 구현
        Claims claims = jwtUtil.extractAllClaims(jwt);
        return claims.get("provider", String.class);
    }

    private void logoutFromSocialProvider(String provider, String accessToken) {
        String logoutUrl = null;
        if ("kakao".equals(provider)) {
            logoutUrl = "https://kauth.kakao.com/oauth/logout?client_id=a090ed16ad7cf9039104c87a03989c38&logout_redirect_uri=http://localhost:8080/login/oauth2/code/kakao";
        } else if ("naver".equals(provider)) {
            logoutUrl = "https://nid.naver.com/oauth2.0/token?grant_type=delete&client_id=q6EE5D8e5hxql9xl1rzh&client_secret=HTM61bxeWo&access_token=" + accessToken + "&service_provider=NAVER";
        } else if ("google".equals(provider)) {
            logoutUrl = "https://accounts.google.com/o/oauth2/revoke?token=" + accessToken;
        }

        if (logoutUrl != null) {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getForEntity(logoutUrl, String.class);
        }
    }

    @PostMapping("/social-logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            log.info("Logging out with JWT: {}", jwt);
            jwtBlacklistService.addToBlacklist(jwt);

            // 소셜 로그아웃 URL 호출
            String provider = getProviderFromJwt(jwt);
            if (provider != null) {
                log.info("Logging out from provider: {}", provider);
                logoutFromSocialProvider(provider, jwt);
            }
        } else {
            log.warn("Authorization header is missing or does not start with Bearer");
        }
        SecurityContextHolder.clearContext();
        log.info("SecurityContextHolder cleared");
        return ResponseEntity.ok("로그아웃 성공");
    }
}
