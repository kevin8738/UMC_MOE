package erd.exmaple.erd.example.domain;

import erd.exmaple.erd.example.domain.common.BaseEntity;
import erd.exmaple.erd.example.domain.enums.Heart;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@Table(name = "record_photo_body")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Record_PhotoBodyEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    @ColumnDefault("''")
    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recordPhotoId")
    private Record_PhotoEntity recordPhoto;

    @Transient
    public String getName() {
        if (recordPhoto != null && recordPhoto.getRecordPage() != null) {
            if (recordPhoto.getRecordPage().getExhibition() != null) {
                return recordPhoto.getRecordPage().getExhibition().getName();
            } else if (recordPhoto.getRecordPage().getPopupStore() != null) {
                return recordPhoto.getRecordPage().getPopupStore().getName();
            }
        }
        return null;
    }

    @Transient
    public LocalDate getStartDate() {
        if (recordPhoto != null && recordPhoto.getRecordPage() != null) {
            if (recordPhoto.getRecordPage().getExhibition() != null) {
                return recordPhoto.getRecordPage().getExhibition().getStartDate();
            } else if (recordPhoto.getRecordPage().getPopupStore() != null) {
                return recordPhoto.getRecordPage().getPopupStore().getStartDate();
            }
        }
        return null;
    }

    @Transient
    public LocalDate getEndDate() {
        if (recordPhoto != null && recordPhoto.getRecordPage() != null) {
            if (recordPhoto.getRecordPage().getExhibition() != null) {
                return recordPhoto.getRecordPage().getExhibition().getEndDate();
            } else if (recordPhoto.getRecordPage().getPopupStore() != null) {
                return recordPhoto.getRecordPage().getPopupStore().getEndDate();
            }
        }
        return null;
    }

}
