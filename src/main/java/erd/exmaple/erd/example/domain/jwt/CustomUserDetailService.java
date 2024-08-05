package erd.exmaple.erd.example.domain.jwt;

import erd.exmaple.erd.example.domain.UserEntity;
import erd.exmaple.erd.example.domain.enums.LoginStatus;
import erd.exmaple.erd.example.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        UserEntity user;

        // 일반 로그인인 경우 phoneNumber로 조회
        user = userRepository.findByPhoneNumber(identifier).orElse(null);

        // 일반 로그인으로 사용자를 찾지 못했을 경우, 소셜 로그인으로 providerId로 조회
        if (user == null) {
            user = userRepository.findByProviderId(identifier)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with phone number or provider ID: " + identifier));
        }

        // 로그인 시 탈퇴 대기 상태인 경우 복구
        if (user.getStatus() == LoginStatus.INACTIVE) {
            user.setStatus(LoginStatus.ACTIVE);
            userRepository.save(user);
        }

        return new org.springframework.security.core.userdetails.User(user.getPhoneNumber(), user.getPassword(), new ArrayList<>());
    }
}
