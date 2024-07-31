package erd.exmaple.erd.example.domain.controller;

import erd.exmaple.erd.example.domain.Popup_StoreEntity;
import erd.exmaple.erd.example.domain.dto.PopupStoreResponseDTO;
import erd.exmaple.erd.example.domain.service.SpecService.SpecPopupStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class SpecPopupStoreController {

    private final SpecPopupStoreService specPopupStoreService;


    @GetMapping("/popupStores/{id}")
    public ResponseEntity<PopupStoreResponseDTO> getExhibition(@PathVariable("id") Long id) {
        Popup_StoreEntity popupStoreEntity = specPopupStoreService.findById(id);

        return ResponseEntity.ok().body(new PopupStoreResponseDTO(popupStoreEntity));

    }
}
