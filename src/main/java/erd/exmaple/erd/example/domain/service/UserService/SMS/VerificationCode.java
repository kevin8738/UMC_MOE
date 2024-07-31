package erd.exmaple.erd.example.domain.service.UserService.SMS;

// 인증번호와 생성 시간을 저장하는 클래스
public class VerificationCode {
    private String code; // 인증번호
    private long creationTime; // 생성 시간

    public VerificationCode(String code, long creationTime) {
        this.code = code;
        this.creationTime = creationTime;
    }

    public String getCode() {
        return code;
    }

    public long getCreationTime() {
        return creationTime;
    }
}