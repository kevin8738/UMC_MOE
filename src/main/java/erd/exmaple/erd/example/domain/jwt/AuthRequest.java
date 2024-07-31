package erd.exmaple.erd.example.domain.jwt;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {

    @NotEmpty(message = "핸드폰 번호는 필수 항목입니다.")
    private String phoneNumber;

    @NotEmpty(message = "비밀번호는 필수 항목입니다.") // 비밀번호가 비어 있지 않도록 검증
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.") // 비밀번호의 최소 길이 검증
    private String password;

    public AuthRequest() {
    }

    public AuthRequest(String phoneNumber, String password) {
        this.phoneNumber =phoneNumber;
        this.password = password;
    }


}