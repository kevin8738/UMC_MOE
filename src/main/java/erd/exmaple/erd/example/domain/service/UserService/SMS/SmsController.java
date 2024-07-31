package erd.exmaple.erd.example.domain.service.UserService.SMS;

import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/sms")
public class SmsController {

    @Autowired
    private CoolSmsService coolSmsService;

    // SMS 전송 요청을 처리하는 엔드포인트
    @PostMapping("/send")
    public String sendSms(@RequestBody SmsRequest request) {
        String phoneNumber = request.getPhoneNumber();
        String confirmPhoneNumber = request.getConfirmPhoneNumber();
        // 전화번호와 확인 전화번호가 일치하는지 확인
        if (!phoneNumber.equals(confirmPhoneNumber)) {
            return "입력하신 전화번호가 일치하지 않습니다.";
        }
        try {
            // 인증번호 생성 및 SMS 전송
            String generatedCode = coolSmsService.sendSms(phoneNumber);
            return "Generated verification code: " + generatedCode;
        } catch (CoolsmsException e) {
            e.printStackTrace();
            return "Failed to send SMS: " + e.getMessage();
        }
    }

    // 인증번호 검증 요청을 처리하는 엔드포인트
    @PostMapping("/verify")
    public String verifyCode(@RequestBody VerificationRequest request) {
        String phoneNumber = request.getPhoneNumber();
        String inputCode = request.getCode();

        // 인증번호 검증
        boolean isVerified = coolSmsService.verifyCode(phoneNumber, inputCode);

        if (isVerified) {
            return "인증 완료";
        } else {
            return "인증 실패";
        }
    }
}