package erd.exmaple.erd.example.domain.dto;

import erd.exmaple.erd.example.domain.Record_PhotoEntity;
import lombok.Getter;
import lombok.Setter;

public class RecordRequestDTO {
    @Getter
    @Setter
    public static class RecordDTO {
        private String body;
        private Long recordPhotoId;
    }
}
