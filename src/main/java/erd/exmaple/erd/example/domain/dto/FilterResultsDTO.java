package erd.exmaple.erd.example.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FilterResultsDTO {
    private List<ExhibitionDTO> exhibitions;
    private List<PopupStoreDTO> popupStores;
}
