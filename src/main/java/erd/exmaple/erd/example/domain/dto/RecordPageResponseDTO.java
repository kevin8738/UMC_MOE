package erd.exmaple.erd.example.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordPageResponseDTO {
    private String photo;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String heart;
}
