package erd.exmaple.erd.example.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPhoneNumberCheckResultDTO {
    private boolean isSuccess;
    private String message;
}