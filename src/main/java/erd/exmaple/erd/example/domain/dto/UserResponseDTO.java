package erd.exmaple.erd.example.domain.dto;

import erd.exmaple.erd.example.domain.enums.Ad;
import erd.exmaple.erd.example.domain.enums.LoginStatus;
import erd.exmaple.erd.example.domain.enums.Marketing;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    Long user_id; // 사용자 ID
    String nickname; // 닉네임
    String phoneNumber; // 핸드폰 번호
    LoginStatus status; // 로그인 상태
    Ad ad; // 광고 수신 여부
    Marketing marketing; // 마케팅 수신 여부

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinResultDTO {
        Long user_id; // 사용자 ID
        LocalDateTime created_at; // 생성 시간
        String phoneNumber; // 핸드폰 번호
        //String nickname;
        //String phoneNumber;
        //LoginStatus status;
        boolean isSuccess; // 성공 여부
        String message; // 응답 메시지
        Ad ad; // 광고 수신 여부
        Marketing marketing; // 마케팅 수신 여부

    }
}