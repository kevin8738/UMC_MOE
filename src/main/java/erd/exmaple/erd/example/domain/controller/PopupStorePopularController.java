package erd.exmaple.erd.example.domain.controller;


import erd.exmaple.erd.example.domain.ExhibitionEntity;
import erd.exmaple.erd.example.domain.Popup_StoreEntity;
import erd.exmaple.erd.example.domain.service.SpecService.ExhibitionPopularService;
import erd.exmaple.erd.example.domain.service.SpecService.PopupStorePopularService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/popupStores")
public class PopupStorePopularController {
    @Autowired
    private PopupStorePopularService PopupStorePopularService;

    /**
     * 좋아요 순으로 팝업스토어를 조회합니다.
     *
     * @param pageable 페이지 정보
     * @return 좋아요 순으로 정렬된 팝업스토어 목록
     */
    @GetMapping("/top-liked")
    public ResponseEntity<Page<Popup_StoreEntity>> getTopLikedPopupStores(Pageable pageable) {
        Page<Popup_StoreEntity> popupStores = PopupStorePopularService.getTopLikedPopupStores(pageable);
        return ResponseEntity.ok(popupStores);
    }
}
