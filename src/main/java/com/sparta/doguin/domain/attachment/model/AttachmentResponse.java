package com.sparta.doguin.domain.attachment.model;


public sealed interface AttachmentResponse permits AttachmentResponse.AttachmentResponseGet {

    record AttachmentResponseGet(
            Long fileId
    ) implements AttachmentResponse {
        public static AttachmentResponse of(Long fileId) {
            return new AttachmentResponseGet(
                    fileId
            );
        }
    }
}
