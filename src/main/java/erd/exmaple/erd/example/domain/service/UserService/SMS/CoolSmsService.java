package erd.exmaple.erd.example.domain.service.UserService.SMS;

import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class CoolSmsService {

    // 인증번호를 저장하기 위한 ConcurrentHashMap
    private ConcurrentHashMap<String, VerificationCode> verificationCodes = new ConcurrentHashMap<>();

    //@Value("${coolsms.api.key}") 깃에 올릴땐 지우고 올리겠씁니다. 테스트 필요하면 말씀해주십쇼
    private String apiKey; // CoolSMS API 키

    //@Value("${coolsms.api.secret}")
    private String apiSecret; // CoolSMS API 시크릿

    @Value("01037764343")
    private String fromPhoneNumber; // 발신 전화번호


    // SMS를 전송하고 인증번호를 생성하는 메서드
    public String sendSms(String to) throws CoolsmsException {
        try {
            // 랜덤한 4자리 인증번호 생성
            String numStr = generateRandomNumber();

            // CoolSMS 메시지 객체 생성
            Message coolsms = new Message(apiKey, apiSecret);

            // 메시지 전송에 필요한 파라미터 설정
            HashMap<String, String> params = new HashMap<>();
            params.put("to", to); // 수신 전화번호
            params.put("from", fromPhoneNumber); // 발신 전화번호
            params.put("type", "sms");
            params.put("text", "인증번호는 [" + numStr + "] 입니다.");

            // 메시지 전송
            coolsms.send(params);

            // 인증번호를 메모리에 저장 (유효기간 5분)
            verificationCodes.put(to, new VerificationCode(numStr, System.currentTimeMillis()));

            return numStr; // 생성된 인증번호 반환

        } catch (CoolsmsException e) {
            // CoolsmsException을 잡아서 로그를 남기고 다시 던짐
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            // 기타 예외를 잡아서 CoolsmsException으로 변환하여 던짐
            e.printStackTrace();
            throw new CoolsmsException("Failed to send SMS", 500); // 500은 임의의 에러 코드
        }
    }

    // 사용자가 입력한 인증번호를 검증하는 메서드
    public boolean verifyCode(String phoneNumber, String inputCode) {
        VerificationCode storedCode = verificationCodes.get(phoneNumber);
        if (storedCode != null) {
            long currentTime = System.currentTimeMillis();
            // 인증번호가 유효기간 내에 있는지 확인하고, 입력된 인증번호와 저장된 인증번호를 비교
            if (currentTime - storedCode.getCreationTime() < TimeUnit.MINUTES.toMillis(5) &&
                    storedCode.getCode().equals(inputCode)) {
                verificationCodes.remove(phoneNumber); // 인증 성공 후 저장된 인증번호 제거
                return true;
            }
        }
        return false; // 인증 실패
    }

    // 랜덤한 4자리 숫자를 생성하는 메서드
    private String generateRandomNumber() {
        Random rand = new Random();
        StringBuilder numStr = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            numStr.append(rand.nextInt(10));
        }
        return numStr.toString();
    }
}