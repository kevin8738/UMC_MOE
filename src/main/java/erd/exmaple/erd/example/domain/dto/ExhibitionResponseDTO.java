package erd.exmaple.erd.example.domain.dto;

import erd.exmaple.erd.example.domain.ExhibitionEntity;
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
    private LocalDate startDate;
    private LocalDate endDate;

    public ExhibitionResponseDTO(ExhibitionEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.place = entity.getPlace();
        this.description = entity.getDescription();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
    }

    // Getters and Setters
}

