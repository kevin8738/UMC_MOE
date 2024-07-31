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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByPhoneNumber(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with phone number: " + username));

        // 로그인 시 탈퇴 대기 상태인 경우 복구
        if (user.getStatus() == LoginStatus.INACTIVE) {
            user.setStatus(LoginStatus.ACTIVE);
            userRepository.save(user);
        }

        return new org.springframework.security.core.userdetails.User(user.getPhoneNumber(), user.getPassword(), new ArrayList<>());
    }
}