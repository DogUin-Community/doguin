package com.sparta.doguin.domain.discussions.dto;

import java.io.Serializable;

public record DiscussionAttachmentResponse(Long id, String filename, String url) implements Serializable {
    private static final long serialVersionUID = 1L;
}
