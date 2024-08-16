package erd.exmaple.erd.example.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FilterResultsDTO {
    private List<ExhibitionResponseDTO> topLikedExhibitions;
    private List<ExhibitionResponseDTO> latestExhibitions;
    private List<PopupStoreResponseDTO> topLikedPopupStores;
    private List<PopupStoreResponseDTO> latestPopupStores;
}


