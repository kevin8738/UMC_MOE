package erd.exmaple.erd.example.domain.dto.passwordDTO;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PasswordFindRequestDTO {

    @NotEmpty(message = "핸드폰 번호는 필수 항목입니다.")
    private String phoneNumber;

    @NotEmpty(message = "핸드폰 번호 확인은 필수 항목입니다.")
    private String confirmPhoneNumber;

    @NotEmpty(message = "인증번호는 필수 항목입니다.")
    private String verificationCode;

    @NotEmpty(message = "새 비밀번호는 필수 항목입니다.")
    @Size(min = 8, message = "새 비밀번호는 최소 8자 이상이어야 합니다.")
    private String newPassword;

    @NotEmpty(message = "새 비밀번호 확인은 필수 항목입니다.")
    @Size(min = 8, message = "새 비밀번호 확인은 최소 8자 이상이어야 합니다.")
    private String confirmPassword;
}