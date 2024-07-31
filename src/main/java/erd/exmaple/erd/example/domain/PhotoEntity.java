package erd.exmaple.erd.example.domain;

import erd.exmaple.erd.example.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@Table(name = "photo")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PhotoEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exhibitionId")
    private ExhibitionEntity exhibition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "popupStoreId")
    private Popup_StoreEntity popupStore;


}
