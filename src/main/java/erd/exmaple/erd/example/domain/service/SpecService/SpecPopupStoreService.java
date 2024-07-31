package erd.exmaple.erd.example.domain.service.SpecService;

import erd.exmaple.erd.example.domain.Popup_StoreEntity;
import erd.exmaple.erd.example.domain.repository.PopupStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpecPopupStoreService {
    private final PopupStoreRepository popupStoreRepository;
    public Popup_StoreEntity findById(Long id) {
        return popupStoreRepository.findById(id).orElse(null);
    }
}
