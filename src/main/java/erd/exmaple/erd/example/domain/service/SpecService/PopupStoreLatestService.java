package erd.exmaple.erd.example.domain.service.SpecService;

import erd.exmaple.erd.example.domain.Popup_StoreEntity;
import erd.exmaple.erd.example.domain.repository.PopupStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PopupStoreLatestService {
    @Autowired
    private PopupStoreRepository popupStoreRepository;
    public Page<Popup_StoreEntity> getAllPopupStores(Pageable pageable) {
        return popupStoreRepository.findAllByOrderByCreatedAtDesc(pageable);
    }
}
