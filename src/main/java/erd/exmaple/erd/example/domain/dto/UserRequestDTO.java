package erd.exmaple.erd.example.domain.dto;

import erd.exmaple.erd.example.domain.enums.Ad;
import erd.exmaple.erd.example.domain.enums.Marketing;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
@Builder
public class UserRequestDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class JoinDto {

        @NotEmpty(message = "비밀번호는 필수 항목입니다.")
        @Size(min = 8, max = 255,message = "비밀번호는 최소 8자 이상이어야 합니다.")
        private String password;

        @NotEmpty(message = "비밀번호 확인은 필수 항목입니다.")
        @Size(min = 8, max = 255)
        private String confirmPassword; // 비밀번호 확인 필드 추가

        @NotEmpty(message = "핸드폰 번호는 필수 항목입니다.")
        @Size(min = 11, max = 11, message = "핸드폰 번호는 정확히 11자여야 합니다.") // 핸드폰 번호 길이 제한
        private String phoneNumber;

        @NotEmpty(message = "닉네임은 필수 항목입니다.")
        private String nickname;

        private Ad ad;
        private Marketing marketing;
    }
}