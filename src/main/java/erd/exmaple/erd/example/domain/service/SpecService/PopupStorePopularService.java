package erd.exmaple.erd.example.domain.service.SpecService;


import erd.exmaple.erd.example.domain.Popup_StoreEntity;
import erd.exmaple.erd.example.domain.dto.PopupStoreResponseDTO;
import erd.exmaple.erd.example.domain.repository.PopupStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PopupStorePopularService {
    @Autowired
    private PopupStoreRepository popupStoreRepository;

    public Page<PopupStoreResponseDTO> getTopLikedPopupStores(Pageable pageable) {
        Page<Popup_StoreEntity> popupStoreEntities = popupStoreRepository.findAllByOrderByLikesCountDesc(pageable);
        return popupStoreEntities.map(PopupStoreResponseDTO::new);
    }
}
