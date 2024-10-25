package com.sparta.doguin.domain.attachment.service.interfaces;


import com.sparta.doguin.config.security.AuthUser;

import java.util.List;

public interface AttachmentDeleteService {
    void delete(AuthUser authUser, List<Long> fileIds);
}
