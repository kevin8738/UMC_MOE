package erd.exmaple.erd.example.domain.dto;


import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExhibitionDTO {
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
    private String regions;
    private String district;

}