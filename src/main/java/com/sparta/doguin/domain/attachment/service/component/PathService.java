package com.sparta.doguin.domain.attachment.service.component;

import com.sparta.doguin.domain.attachment.constans.AttachmentTargetType;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Objects;
import java.util.UUID;

import static com.sparta.doguin.domain.common.globalpath.GlobalPath.S3_BASE_URL;

@Service
@RequiredArgsConstructor
public class PathService {
    // ex) user/1/targetId/1/outsourcing/dba64057-0135-455b-b27d-f88300e2ba3f/90b987ef-e145-4b4f-92b2-b4d844d1aafb_Viktor_600x600(3).jpg
    public String mkPath(MultipartFile file, AuthUser authUser, Long targetId, AttachmentTargetType targetType) {
        User user = User.fromAuthUser(authUser);
        return "user" + "/" +
                user.getId() + "/" +
                targetType.getTarget() + "/" +
                targetId + "/" +
                UUID.randomUUID() + Objects.requireNonNull(file.getOriginalFilename()).replace(" ", "");
    }

    public String mkPath(MultipartFile file, User user, Long targetId, AttachmentTargetType targetType) {
        return "user" + "/" +
                user.getId() + "/" +
                targetType.getTarget() + "/" +
                targetId + "/" +
                UUID.randomUUID() + Objects.requireNonNull(file.getOriginalFilename()).replace(" ", "");
    }

    public String mkfullPath(String path) {
        // 경로를 슬래시로 통일
        String normalizedPath = path.replace("\\", "/");
        URI fullUri = URI.create(S3_BASE_URL).resolve(normalizedPath);
        return fullUri.toString();
    }
}
