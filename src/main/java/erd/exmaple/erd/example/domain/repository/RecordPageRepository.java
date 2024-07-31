package erd.exmaple.erd.example.domain.repository;

import erd.exmaple.erd.example.domain.Record_PageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecordPageRepository extends JpaRepository<Record_PageEntity, Long> {
    Page<Record_PageEntity> findAllByUserIdOrderByCreatedAtAsc(Pageable pageable, Long userId);
    Page<Record_PageEntity> findAllByUserIdOrderByCreatedAtDesc(Pageable pageable, Long userId);
    Optional<Record_PageEntity> findByIdAndUserId(Long id, Long userId);
}
