package erd.exmaple.erd.example.domain.dto.passwordDTO;

public class PasswordResetRequestDTO {
    private String phoneNumber;
    private String confirmPhoneNumber;

    public PasswordResetRequestDTO() {}

    public PasswordResetRequestDTO(String phoneNumber, String confirmPhoneNumber) {
        this.phoneNumber = phoneNumber;
        this.confirmPhoneNumber = confirmPhoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getConfirmPhoneNumber() {
        return confirmPhoneNumber;
    }

    public void setConfirmPhoneNumber(String confirmPhoneNumber) {
        this.confirmPhoneNumber = confirmPhoneNumber;
    }
}