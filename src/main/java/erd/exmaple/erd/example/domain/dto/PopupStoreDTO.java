package erd.exmaple.erd.example.domain.dto;

import erd.exmaple.erd.example.domain.enums.District;
import erd.exmaple.erd.example.domain.enums.Region;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopupStoreDTO {
    private Long id;
    private String name;
    private String place;
    private String description;
    private String photoUrl; // 사진 URL 필드 추가
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean heart;
    private LocalDateTime searchDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Region regions;
    private District district;

}