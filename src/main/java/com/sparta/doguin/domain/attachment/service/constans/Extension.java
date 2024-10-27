package com.sparta.doguin.domain.attachment.service.constans;

import lombok.Getter;

@Getter
public enum Extension {
    JPG("jpg"),
    JPEG("jpeg"),
    PNG("png"),
    GIF("gif"),
    BMP("bmp"),
    TIFF("tiff"),
    SVG("svg"),
    WEBP("webp"),
    ICO("ico");

    private final String extension;

    Extension(String extension) {
        this.extension = extension;
    }
}
