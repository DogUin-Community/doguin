package com.sparta.doguin.domain.outsourcing.constans;

import lombok.Getter;

@Getter
public enum AreaType {
    SEOUL("서울"),
    GYEONGGI("경기도"),
    INCHEON("인천"),
    BUSAN("부산"),
    DAEGU("대구"),
    DAEJEON("대전"),
    GWANGJU("광주"),
    ULSAN("울산"),
    SEJONG("세종"),
    GANGWON("강원도"),
    CHUNGCHEONGBUK("충청북도"),
    CHUNGCHEONGNAM("충청남도"),
    JEOLLABUK("전라북도"),
    JEOLLANAM("전라남도"),
    GYEONGSANGBUK("경상북도"),
    GYEONGSANGNAM("경상남도"),
    JEJU("제주도");

    private final String description;

    AreaType(String description) {
        this.description = description;
    }
}
