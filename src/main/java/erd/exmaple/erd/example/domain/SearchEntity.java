package erd.exmaple.erd.example.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@Table(name = "search")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SearchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exhibitionId", nullable = true)
    private ExhibitionEntity exhibition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "popupStoreId", nullable = true)
    private Popup_StoreEntity popupStore;

    @Column(nullable = false)
    private LocalDateTime searchDate;
}
