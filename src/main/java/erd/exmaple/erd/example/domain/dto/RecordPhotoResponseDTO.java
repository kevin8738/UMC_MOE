package erd.exmaple.erd.example.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class RecordPhotoResponseDTO {
    @Builder
    @Getter
    public static class PhotoResponseDTO {
        Long recordPhotoId;
        LocalDateTime createdAt;
    }
}
