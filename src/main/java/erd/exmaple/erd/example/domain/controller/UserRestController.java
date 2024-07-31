package erd.exmaple.erd.example.domain.controller;


import erd.exmaple.erd.example.domain.UserEntity;
import erd.exmaple.erd.example.domain.dto.*;


import erd.exmaple.erd.example.domain.dto.passwordDTO.PasswordChangeRequestDTO;
import erd.exmaple.erd.example.domain.dto.passwordDTO.PasswordFindRequestDTO;
import erd.exmaple.erd.example.domain.dto.passwordDTO.PasswordSetRequestDTO;
import erd.exmaple.erd.example.domain.jwt.AuthRequest;
import erd.exmaple.erd.example.domain.jwt.AuthResponse;
import erd.exmaple.erd.example.domain.jwt.JwtBlacklistService;
import erd.exmaple.erd.example.domain.service.UserService.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.userdetails.UserDetails;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import erd.exmaple.erd.example.domain.jwt.JwtUtil;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserRestController {
    private static final Logger logger = LoggerFactory.getLogger(UserRestController.class);
    private final UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtBlacklistService jwtBlacklistService;


    @PostMapping("/join")
    public ResponseEntity<UserResponseDTO.JoinResultDTO> joinUser(@RequestBody @Valid UserRequestDTO.JoinDto joinDto) {
        UserResponseDTO.JoinResultDTO response = userService.joinUser(joinDto);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-phoneNumber")
    public ResponseEntity<UserPhoneNumberCheckResultDTO> checkPhoneNumber(@RequestParam String phoneNumber) {
        return ResponseEntity.ok(userService.checkPhoneNumber(phoneNumber));
    }

    @GetMapping("{userId}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long userId) {
        UserResponseDTO user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login") // 로그인 엔드포인트
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            // 사용자 인증 시도
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getPhoneNumber(), authRequest.getPassword())
            );
            //} catch (BadCredentialsException e) {
            // 인증 실패 시 예외 처리
            //throw new Exception("핸드폰번호(아이디) 또는 비밀번호 오류", e);
            //}

            // 인증된 사용자 정보 로드
            final UserDetails userDetails = userDetailsService
                    .loadUserByUsername(authRequest.getPhoneNumber());

            // JWT 토큰 생성
            final String jwt = jwtUtil.generateToken(userDetails);

            // 성공적인 로그인에 대한 로그 추가
            System.out.println("User " + authRequest.getPhoneNumber() + " successfully logged in.");


            // 생성된 JWT 토큰을 응답으로 반환
            return ResponseEntity.ok(new AuthResponse(jwt, "로그인 성공"));
        } catch (BadCredentialsException e) {
            // 인증 실패 시 예외 처리 및 실패 응답 반환
            System.out.println("User " + authRequest.getPhoneNumber() + " failed to log in.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("핸드폰번호(아이디) 또는 비밀번호 오류");
        } catch (Exception e) {
            // 기타 예외 처리 및 내부 서버 오류 응답 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그인 중 오류 발생");
        }
    }

    // 비밀번호 찾기 및 새 비밀번호 설정 엔드포인트
    @PostMapping("/find-password")
    public ResponseEntity<String> findAndResetPassword(@RequestBody @Valid PasswordFindRequestDTO passwordFindRequest) {
        try {
            userService.findPassword(passwordFindRequest);
            return ResponseEntity.ok("새로운 비밀번호가 설정되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 비밀번호 변경 엔드포인트
    @PostMapping("/my-page/change-password")
    public ResponseEntity<String> changePassword(@RequestBody @Valid PasswordChangeRequestDTO passwordChangeRequest) {
        try {
            // SecurityContextHolder에서 인증된 사용자 정보 가져오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String phoneNumber = authentication.getName();
            passwordChangeRequest.setPhoneNumber(phoneNumber); // 요청 DTO에 핸드폰 번호 설정
            userService.changePassword(passwordChangeRequest);
            return ResponseEntity.ok("비밀번호 변경에 성공하였습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 닉네임 변경 엔드포인트
    @PostMapping("/my-page/change-nickname")
    public ResponseEntity<String> changeNickname(@RequestParam String newNickname) {
        try {
            // SecurityContextHolder에서 인증된 사용자 정보 가져오기
            // SecurityContextHolder에서 인증된 사용자 정보 가져오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            logger.info("Current Authentication: " + authentication);

            String phoneNumber = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<UserEntity> userOptional = userService.findByPhoneNumber(phoneNumber);
            if (userOptional.isPresent()) {
                UserEntity user = userOptional.get();

                userService.updateNickname(user.getId(), newNickname);
                return ResponseEntity.ok("닉네임이 성공적으로 변경되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 회원탈퇴 엔드포인트
    @PostMapping("/my-page/withdraw")
    public ResponseEntity<String> withdrawUser() {
        try {
            // SecurityContextHolder에서 인증된 사용자 정보 가져오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String phoneNumber = authentication.getName();
            userService.withdrawUser(phoneNumber);
            return ResponseEntity.ok("회원탈퇴가 요청되었습니다. 7일 후에 탈퇴가 완료됩니다. 7일 후에는 복구할 수 없습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 닉네임 조회 엔드포인트
    @GetMapping("/my-page/search")
    public ResponseEntity<List<String>> searchUsers(@RequestParam String keyword) {
        try {
            List<String> nicknames = userService.searchUsersByNickname(keyword);
            return ResponseEntity.ok(nicknames);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // 로그아웃 엔드포인트
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            jwtBlacklistService.addToBlacklist(jwt);
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("로그아웃 성공");
    }
}





//    @PostMapping("/my-page/change-nickname")
//    public ResponseEntity<String> changeNickname(@RequestParam String newNickname) {
//        try {
//            // SecurityContextHolder에서 인증된 사용자 정보 가져오기
//            String phoneNumber = SecurityContextHolder.getContext().getAuthentication().getName();
//            Optional<UserEntity> userOptional = userService.findByPhoneNumber(phoneNumber);
//            if (userOptional.isPresent()) {
//                UserEntity user = userOptional.get();
//                userService.updateNickname(user.getId(), newNickname);
//                return ResponseEntity.ok("닉네임이 성공적으로 변경되었습니다..");
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유저를 찾을 수 없습니다..");
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }





//    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequestDTO request) {
//        String phoneNumber = request.getPhoneNumber();
//        String confirmPhoneNumber = request.getConfirmPhoneNumber();
//
//        if (!phoneNumber.equals(confirmPhoneNumber)) {
//            return ResponseEntity.badRequest().body("Phone numbers do not match.");
//        }
//
//        String newPassword = userService.resetPasswordByPhoneNumber(phoneNumber);
//        if (newPassword != null) {
//            return ResponseEntity.ok("Password reset successfully. Your new password is: " + newPassword);
//        } else {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to reset password.");
//        }
//    }



