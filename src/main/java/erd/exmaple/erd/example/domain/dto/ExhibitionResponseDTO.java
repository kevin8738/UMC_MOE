package erd.exmaple.erd.example.domain.dto;

import erd.exmaple.erd.example.domain.ExhibitionEntity;
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
public class ExhibitionResponseDTO {
    private Long id;
    private String name;
    private String place;
    private String description;
    private String photoUrl; // 사진 URL 필드 추가
    private LocalDate startDate;
    private LocalDate endDate;
    private Region regions;
    private District district;

    public ExhibitionResponseDTO(ExhibitionEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.place = entity.getPlace();
        this.description = entity.getDescription();
        this.photoUrl = entity.getPhotoUrl();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.regions = entity.getRegions();
        this.district = entity.getDistrict();
    }

    // Getters and Setters
}

