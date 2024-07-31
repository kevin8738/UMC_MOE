package erd.exmaple.erd.example.domain.controller;

import erd.exmaple.erd.example.domain.ExhibitionEntity;
import erd.exmaple.erd.example.domain.Popup_StoreEntity;
import erd.exmaple.erd.example.domain.service.SpecService.ExhibitionLatestService;
import erd.exmaple.erd.example.domain.service.SpecService.PopupStoreLatestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/popupStores")
public class PopupStoreLatestController {
    @Autowired
    private PopupStoreLatestService popupStoreLatestService;


    @GetMapping("/latest")
    public ResponseEntity<Page<Popup_StoreEntity>> getAllExhibitions(Pageable pageable) {
        Page<Popup_StoreEntity> popupStores = popupStoreLatestService.getAllPopupStores(pageable);
        return ResponseEntity.ok(popupStores);
    }
}
