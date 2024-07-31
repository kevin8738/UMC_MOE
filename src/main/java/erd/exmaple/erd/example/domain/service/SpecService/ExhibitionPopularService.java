package erd.exmaple.erd.example.domain.service.SpecService;

import erd.exmaple.erd.example.domain.ExhibitionEntity;
import erd.exmaple.erd.example.domain.repository.ExhibitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExhibitionPopularService {

    @Autowired
    private ExhibitionRepository exhibitionRepository;

    public Page<ExhibitionEntity> getTopLikedExhibitions(Pageable pageable) {
        return exhibitionRepository.findAllByOrderByLikesCountDesc(pageable);
    }
}

