package erd.exmaple.erd.example.domain.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class RecordResponseDTO {
    @Builder
    @Getter
    public static class RecordResultDTO {
        Long recordPhotoBodyId;
        LocalDateTime createdAt;
    }
}
