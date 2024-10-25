package com.sparta.doguin.domain.attachment.validate;

import com.sparta.doguin.domain.common.exception.FileException;

import static com.sparta.doguin.domain.common.response.ApiResponseFileEnum.FILE_IS_NOT_ME;


public class AttachmentValidator {
    public static void isMe(Long id, Long targetId) {
        if (!id.equals(targetId) ) {
            throw new FileException(FILE_IS_NOT_ME);
        }
    }
}
