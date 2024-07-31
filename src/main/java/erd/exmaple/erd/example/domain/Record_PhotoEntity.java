package erd.exmaple.erd.example.domain;

import erd.exmaple.erd.example.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@Table(name="record_photo")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Record_PhotoEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String photoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recordPageId")
    private Record_PageEntity recordPage;

    @OneToMany(mappedBy = "recordPhoto", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Record_PhotoBodyEntity> bodyList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photoId")
    private PhotoEntity photoEntity;

    public String getPhotoUrl() {
        return photoEntity != null ? photoEntity.getPhoto() : null;
    }
}
