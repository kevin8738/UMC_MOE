package erd.exmaple.erd.example.domain.repository;


import erd.exmaple.erd.example.domain.UserEntity;
import erd.exmaple.erd.example.domain.enums.LoginStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByPhoneNumber(String phoneNumber);
    Optional<UserEntity> findByNickname(String nickname);
    //Optional<UserEntity> findByPhoneNumberUser(String phoneNumber);
    List<UserEntity> findAllByStatus(LoginStatus status);
    List<UserEntity> findByNicknameContaining(String keyword);
    Optional<UserEntity> findByProviderId(String providerId);
}