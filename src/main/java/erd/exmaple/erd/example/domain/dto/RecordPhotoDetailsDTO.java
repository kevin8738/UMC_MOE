package erd.exmaple.erd.example.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordPhotoDetailsDTO {
    private String recordPhoto;
    private String recordPhotoBody;
}
