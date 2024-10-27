package com.sparta.doguin.domain.attachment.validate;

import com.sparta.doguin.domain.attachment.service.component.AttachmentUtil;
import com.sparta.doguin.domain.attachment.service.constans.Extension;
import com.sparta.doguin.domain.common.exception.FileException;
import org.springframework.web.multipart.MultipartFile;

import static com.sparta.doguin.domain.common.response.ApiResponseFileEnum.*;

public class AttachmentValidator {
    private static final Long MAX_SIZE = 2_000_000L;
    /**
     * @title 파일 첨부자가 본인인지 확인하는 검증 메서드
     *
     * @param userId 유저
     * @param targetId 삭제,수정 하려는 첨부파일에 저장되있는 유저
     */
    public static void isMe(Long userId, Long targetId) {
        if (!userId.equals(targetId) ) {
            throw new FileException(FILE_IS_NOT_ME);
        }
    }

    /**
     * @title 클라이언트로 부터 요청받은 파일갯수와, 레파지토리에서 갯수를 셋을때 같은지 확인하는 메서드
     * @description 파일을 찾을때 In 절을 활용하여 여러개를 찾기때문에, 만약 유효하지않은 파일아이디가 들어오면 값을 못찾기때문에 갯수가 동일한지 확인
     *
     * @param originCount 클라이언트로부터 요청된 파일 Id의 갯수
     * @param targetCount repositry로부터 찾은 파일의 갯수
     */
    public static void isCountEqual(Integer originCount, Integer targetCount) {
        if (!originCount.equals(targetCount) ) {
            throw new FileException(FILE_COUNT_NOT_MATCH);
        }
    }

    /**
     * 파일의 확장자가 이미지인지 확인
     *
     * @param file 확장자 확인 파일
     */
    public static void isInExtension(MultipartFile file) {
        String extension = AttachmentUtil.getExtension(file);
        boolean check = false;
        for ( Extension inExtension : Extension.values() ) {
            if (inExtension.getExtension().equals(extension)) {
                check = true;
                break;
            }
        }
        if (!check) {
            throw new FileException(FILE_NOT_VALIDATE_EXTENSION);
        }
    }

    public static void isSizeBig(MultipartFile file) {
        if (file.getSize() > MAX_SIZE) {
            throw new FileException(FILE_NOT_MATCH_SIZE);
        }
    }
}


