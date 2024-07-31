package erd.exmaple.erd.example.domain.service.UserService;

import erd.exmaple.erd.example.domain.UserEntity;
import erd.exmaple.erd.example.domain.enums.LoginStatus;
import erd.exmaple.erd.example.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserWithdrawalScheduler {
    private final UserRepository userRepository;

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void processPendingWithdrawals() {
        List<UserEntity> users = userRepository.findAllByStatus(LoginStatus.INACTIVE);
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);

        for (UserEntity user : users) {
            // 탈퇴 상태가 7일이 지난 경우 삭제
            if (user.getUpdatedAt().isBefore(sevenDaysAgo)) {
                userRepository.delete(user);
            }
        }
    }
}