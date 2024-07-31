package erd.exmaple.erd.example.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class RecordPhotoRequestDTO {
    @Getter
    @Setter
    public static class PhotoRequestDTO {
        private List<String> photoUrl;
    }
}
