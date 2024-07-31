package erd.exmaple.erd.example.domain.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum District {

    // 서울 구
    서울_종로구, 서울_중구, 서울_용산구, 서울_성동구, 서울_광진구,
    서울_동대문구, 서울_중랑구, 서울_성북구, 서울_강북구, 서울_도봉구,
    서울_노원구, 서울_은평구, 서울_서대문구, 서울_마포구, 서울_양천구,
    서울_강서구, 서울_구로구, 서울_금천구, 서울_영등포구, 서울_동작구,
    서울_관악구, 서울_서초구, 서울_강남구, 서울_송파구, 서울_강동구,

    // 부산 구
    부산_동래구, 부산_금정구, 부산_연제구, 부산_해운대구, 부산_수영구,
    부산_남구, 부산_기장군, 부산_부산진구, 부산_동구, 부산_중구,
    부산_서구, 부산_북구, 부산_영도구, 부산_사하구, 부산_사상구, 부산_강서구;

    public static List<District> getDistrictsByRegion(Region region) {
        return Arrays.stream(District.values())
                .filter(district -> district.name().startsWith(region.name() + "_"))
                .collect(Collectors.toList());
    }
}