package erd.exmaple.erd.example.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExhibitionOrPopupDetailsDTO {
    private String name;
    private String photo;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<String> recordPhotos;
}
