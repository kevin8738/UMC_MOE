package erd.exmaple.erd.example.domain.service.UserService;

import erd.exmaple.erd.example.domain.UserEntity;
import erd.exmaple.erd.example.domain.converter.UserConverter;
import erd.exmaple.erd.example.domain.dto.UserPhoneNumberCheckResultDTO;
import erd.exmaple.erd.example.domain.dto.UserRequestDTO;
import erd.exmaple.erd.example.domain.dto.UserResponseDTO;
import erd.exmaple.erd.example.domain.dto.passwordDTO.PasswordChangeRequestDTO;
import erd.exmaple.erd.example.domain.dto.passwordDTO.PasswordFindRequestDTO;
import erd.exmaple.erd.example.domain.enums.Ad;
import erd.exmaple.erd.example.domain.enums.LoginStatus;
import erd.exmaple.erd.example.domain.enums.Marketing;
import erd.exmaple.erd.example.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService  {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // PasswordEncoder 주입 // PasswordEncoder 주입

    @Override
    public UserResponseDTO.JoinResultDTO joinUser(UserRequestDTO.JoinDto joinDto) {
        // 비밀번호 확인
        if (!joinDto.getPassword().equals(joinDto.getConfirmPassword())) {
            return UserResponseDTO.JoinResultDTO.builder()
                    .isSuccess(false)
                    .message("비밀번호가 일치하지 않습니다.")
                    .build();
        }
        // 핸드폰 번호 중복 확인
        Optional<UserEntity> existingUser = userRepository.findByPhoneNumber(joinDto.getPhoneNumber());
        if (existingUser.isPresent()) {
            return UserResponseDTO.JoinResultDTO.builder()
                    .isSuccess(false)
                    .message("핸드폰 번호가 이미 존재합니다.")
                    .build();
        }



/*         아이디(휴대폰번호) 중복 확인
        if (userRepository.findByPhoneNumber(joinDto.getPhoneNumber()).isPresent()) {
            return UserResponseDTO.JoinResultDTO.builder()
                   .isSuccess(false)
                    .message("닉네임이 이미 사용 중입니다.")
                  .build();
        }*/

        // UserEntity 생성
        UserEntity newUser = UserEntity.builder()
                .password(passwordEncoder.encode(joinDto.getPassword())) // 비밀번호 암호화 ....
                .phoneNumber(joinDto.getPhoneNumber())
                .nickname(joinDto.getNickname())
                .status(LoginStatus.ACTIVE)
                .ad(joinDto.getAd() != null ? joinDto.getAd() : Ad.INACTIVE)
                .marketing(joinDto.getMarketing() != null ? joinDto.getMarketing() : Marketing.INACTIVE)
                .build();

        // 사용자 저장
        UserEntity savedUser = userRepository.save(newUser);

        // 응답 DTO 생성
        return UserResponseDTO.JoinResultDTO.builder()
                .user_id(savedUser.getId())
                .created_at(LocalDateTime.now())
                .isSuccess(true)
                .phoneNumber(savedUser.getPhoneNumber())
                .message("회원가입이 성공적으로 완료되었습니다.")
                .build();
    }

    @Override
    public UserPhoneNumberCheckResultDTO checkPhoneNumber(String phoneNumber) {
        Optional<UserEntity> existingUser = userRepository.findByPhoneNumber(phoneNumber);
        if (existingUser.isPresent()) {
            return UserPhoneNumberCheckResultDTO.builder()
                    .isSuccess(false)
                    .message("핸드폰 번호가 이미 존재합니다.")
                    .build();
        }
        return UserPhoneNumberCheckResultDTO.builder()
                .isSuccess(true)
                .message("사용 가능한 핸드폰 번호입니다.")
                .build();
    }

    @Override
    public UserResponseDTO getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(userConverter::convert)
                .orElse(null);
    }
    @Override
    public String resetPasswordByPhoneNumber(String phoneNumber) {
        Optional<UserEntity> userOptional = userRepository.findByPhoneNumber(phoneNumber);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            String newPassword = generateRandomPassword();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return newPassword;
        }
        return null;
    }

    private String generateRandomPassword() {
        // 랜덤 비밀번호 생성 로직 (8자리 랜덤 문자열)
        return UUID.randomUUID().toString().substring(0, 8);
    }

    // 비밀번호 변경 구현
    @Override
    public void changePassword(PasswordChangeRequestDTO passwordChangeRequest) throws Exception {
        Optional<UserEntity> userOptional = userRepository.findByPhoneNumber(passwordChangeRequest.getPhoneNumber());
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            // 현재 비밀번호 검증
            if (passwordEncoder.matches(passwordChangeRequest.getCurrentPassword(), user.getPassword())) {
                // 새 비밀번호와 확인 비밀번호가 일치하는지 확인
                if (!passwordChangeRequest.getNewPassword().equals(passwordChangeRequest.getConfirmPassword())) {
                    throw new Exception("새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
                }
                // 새 비밀번호 설정
                user.setPassword(passwordEncoder.encode(passwordChangeRequest.getNewPassword()));
                userRepository.save(user);
            } else {
                throw new Exception("현재 비밀번호가 일치하지 않습니다.");
            }
        } else {
            throw new Exception("사용자를 찾을 수 없습니다.");
        }
    }

    // 비밀번호 찾기 구현
    @Override
    public void findPassword(PasswordFindRequestDTO passwordFindRequest) throws Exception {
        Optional<UserEntity> userOptional = userRepository.findByPhoneNumber(passwordFindRequest.getPhoneNumber());
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            // 인증번호 검증을 생략하고 새로운 비밀번호 설정
            if (!passwordFindRequest.getPhoneNumber().equals(passwordFindRequest.getConfirmPhoneNumber())) {
                throw new Exception("핸드폰 번호가 일치하지 않습니다.");
            }
            if (!passwordFindRequest.getNewPassword().equals(passwordFindRequest.getConfirmPassword())) {
                throw new Exception("새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            }
            user.setPassword(passwordEncoder.encode(passwordFindRequest.getNewPassword()));
            userRepository.save(user);
            System.out.println("New password has been set.");
        } else {
            throw new Exception("사용자를 찾을 수 없습니다.");
        }
    }

    private String generateTemporaryPassword() {
        // 임시 비밀번호 생성 로직 (8자리 랜덤 문자열)
        return UUID.randomUUID().toString().substring(0, 8);
    }


    @Override
    public Optional<UserEntity> findByPhoneNumber(String phoneNumber) {
        logger.info("Finding user by phone number: " + phoneNumber);
        return userRepository.findByPhoneNumber(phoneNumber);
    }
    @Override
    public void updateNickname(Long userId, String newNickname) throws Exception {
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            user.setNickname(newNickname);
            userRepository.save(user);
        } else {
            throw new Exception("사용자를 찾을 수 없습니다.");
        }
    }

    @Override
    public void withdrawUser(String phoneNumber) throws Exception {
        Optional<UserEntity> userOptional = userRepository.findByPhoneNumber(phoneNumber);
        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();
            user.setStatus(LoginStatus.INACTIVE);
            userRepository.save(user);
        } else {
            throw new Exception("사용자를 찾을 수 없습니다.");
        }
    }

    @Override
    public List<String> searchUsersByNickname(String keyword) {
        List<UserEntity> users = userRepository.findByNicknameContaining(keyword);
        List<String> nicknames = new ArrayList<>();
        for (UserEntity user : users) {
            nicknames.add(user.getNickname());
        }
        return nicknames;
    }
}