package com.sparta.doguin.domain.attachment.entity;

import com.sparta.doguin.domain.attachment.constans.AttachmentTargetType;
import com.sparta.doguin.domain.common.Timestamped;
import com.sparta.doguin.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
public class Attachment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Long targetId;

    private String attachment_absolute_path;

    private String attachment_relative_path;

    private String attachment_original_name;

    private Long attachment_size;

    @Enumerated(value = EnumType.STRING)
    private AttachmentTargetType target;


}
