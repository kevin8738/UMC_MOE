package erd.exmaple.erd.example.domain.converter;

import erd.exmaple.erd.example.domain.Record_PhotoEntity;
import erd.exmaple.erd.example.domain.dto.RecordPhotoResponseDTO;

public class RecordPhotoConverter {

    public static Record_PhotoEntity toRecordPhotoEntity(String photoUrl) {
        return Record_PhotoEntity.builder()
                .photoUrl(photoUrl)
                .build();
    }

    public static RecordPhotoResponseDTO.PhotoResponseDTO toRecordPhotoResponseDTO(Record_PhotoEntity photo) {
        return RecordPhotoResponseDTO.PhotoResponseDTO.builder()
                .recordPhotoId(photo.getId())
                .createdAt(photo.getCreatedAt())
                .build();
    }
}
