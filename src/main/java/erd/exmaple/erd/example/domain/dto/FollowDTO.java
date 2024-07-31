package erd.exmaple.erd.example.domain.dto;

import erd.exmaple.erd.example.domain.enums.Heart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowDTO {
    private Long id;
    private ExhibitionDTO exhibition;
    private PopupStoreDTO popupStore;
    private Heart heart;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt; // 추가
}