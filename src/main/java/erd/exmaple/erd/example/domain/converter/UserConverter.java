package erd.exmaple.erd.example.domain.converter;

import erd.exmaple.erd.example.domain.UserEntity;
import erd.exmaple.erd.example.domain.dto.UserResponseDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
//userEntityToUserResponseDTOConverter
@Component
public class UserConverter implements Converter<UserEntity, UserResponseDTO> {

    @Override
    public UserResponseDTO convert(UserEntity source) {
        return UserResponseDTO.builder()
                .user_id(source.getId())
                .nickname(source.getNickname())
                .phoneNumber(source.getPhoneNumber())
                .status(source.getStatus())
                .ad(source.getAd())
                .marketing(source.getMarketing())
                .build();
    }
}