package erd.exmaple.erd.example.domain.service.UserService;


import erd.exmaple.erd.example.domain.UserEntity;
import erd.exmaple.erd.example.domain.dto.UserPhoneNumberCheckResultDTO;
import erd.exmaple.erd.example.domain.dto.UserRequestDTO;
import erd.exmaple.erd.example.domain.dto.UserResponseDTO;
import erd.exmaple.erd.example.domain.dto.passwordDTO.PasswordChangeRequestDTO;
import erd.exmaple.erd.example.domain.dto.passwordDTO.PasswordFindRequestDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserResponseDTO.JoinResultDTO joinUser(UserRequestDTO.JoinDto joinDto);
    UserPhoneNumberCheckResultDTO checkPhoneNumber(String phoneNumber);
    UserResponseDTO getUserById(Long userId);
    String resetPasswordByPhoneNumber(String phoneNumber);
    void changePassword(PasswordChangeRequestDTO passwordChangeRequest) throws Exception;
    void findPassword(PasswordFindRequestDTO passwordFindRequest) throws Exception;
    void updateNickname(Long userId, String newNickname) throws Exception;
    Optional<UserEntity> findByPhoneNumber(String phoneNumber);
    //void setNewPassword(PasswordSetRequestDTO passwordSetRequest) throws Exception;
    void withdrawUser(String phoneNumber) throws Exception;
    List<String> searchUsersByNickname(String keyword);
}