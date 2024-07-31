package erd.exmaple.erd.example.domain;


import erd.exmaple.erd.example.domain.common.BaseEntity;
import erd.exmaple.erd.example.domain.enums.Heart;
import erd.exmaple.erd.example.domain.enums.LoginStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@Table(name="follow")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor

public class FollowEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(15) DEFAULT 'INACTIVE'")
    private Heart heart;
//애매
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "exhibitionId")
   private ExhibitionEntity exhibition;
//이것도 애매
   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "popupStoreId")
   private Popup_StoreEntity popupStore;
}
