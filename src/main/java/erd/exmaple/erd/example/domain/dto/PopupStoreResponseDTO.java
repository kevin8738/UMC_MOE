package erd.exmaple.erd.example.domain.dto;

import erd.exmaple.erd.example.domain.Popup_StoreEntity;
import erd.exmaple.erd.example.domain.enums.District;
import erd.exmaple.erd.example.domain.enums.Region;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopupStoreResponseDTO {
    private Long id;
    private String name;
    private String place;
    private String description;
    private String photoUrl; // 사진 URL 필드 추가
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean heart;
    private Region regions;
    private District district;


    public PopupStoreResponseDTO(Popup_StoreEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.place = entity.getPlace();
        this.description = entity.getDescription();
        this.photoUrl = entity.getPhotoUrl();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.heart = entity.isHeart();
        this.regions = entity.getRegions();
        this.district = entity.getDistrict();
    }
}
