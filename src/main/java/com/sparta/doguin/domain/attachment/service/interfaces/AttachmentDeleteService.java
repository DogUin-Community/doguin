package com.sparta.doguin.domain.attachment.service.interfaces;


import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.security.AuthUser;

import java.util.List;

public interface AttachmentDeleteService {
    void delete(AuthUser authUser, List<Long> fileIds);
    void delete(User user, List<Long> fileIds);
}
