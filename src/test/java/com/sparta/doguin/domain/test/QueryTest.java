package com.sparta.doguin.domain.test;

import com.sparta.doguin.domain.attachment.constans.AttachmentTargetType;
import com.sparta.doguin.domain.attachment.entity.Attachment;
import com.sparta.doguin.domain.attachment.repository.AttachmentRepository;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.domain.user.enums.UserRole;
import com.sparta.doguin.domain.user.enums.UserType;
import com.sparta.doguin.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;


@Rollback(false)
@SpringBootTest
public class QueryTest {
    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    UserRepository userRepository;

    List<Long> fileIds = new ArrayList<>();

//    @BeforeEach
//    void setup1(){
//        for (int i = 0; i < 100; i++) {
//            fileIds.add(i+1L);
//        }
//    }

    @BeforeEach
    void setup() {
        User user = new User(
                null,                        // id
                "example@example.com",      // email
                "password123",              // password
                "john_doe",                 // nickname
                UserType.COMPANY,           // userType (예시로 REGULAR 타입 사용)
                UserRole.ROLE_USER,         // userRole (예시로 USER 역할 사용)
                "profile.jpg",              // profileImage
                "안녕하세요, 저는 John입니다.",  // introduce
                "서울시 강남구",              // homeAddress
                "https://github.com/johndoe",// gitAddress
                "https://blog.example.com"   // blogAddress
        );
        userRepository.save(user);

        for (int i = 0; i < 100; i++) {

            Attachment attachment = new Attachment(
                    null,
                    user,                          // 기존에 생성된 User 객체 사용
                    100L + i,                      // 고유한 targetId 설정
                    "/absolute/path/to/file" + i + ".jpg",   // 고유한 절대 경로
                    "/relative/path/to/file" + i + ".jpg",   // 고유한 상대 경로
                    "file" + i + ".jpg",           // 고유한 파일 이름
                    2048L + i,                     // 파일 크기 (고유하게 설정)
                    AttachmentTargetType.PORTFOLIO  // target (예시로 PORTFOLIO 타입 설정)
            );
            fileIds.add(i+1L);
            attachmentRepository.save(attachment);
        }
    }

//    @Transactional
//    @Test
//    @DisplayName("쿼리를 사용 하지않고, 반복문을 통하여 파일들 가져오기")
//    void test1(){
//        Long start = System.currentTimeMillis();
//        List<Attachment> attachments = new ArrayList<>();
//        System.out.println("!!!!!!!!!!!!!!!!!!!! 쿼리 시작 !!!!!!!!!!!!!!!!!!!!!!");
//        for ( Long fileId : fileIds ) {
//            Attachment attachment = attachmentRepository.findById(fileId).orElseThrow(() -> new FileException(FILE_NOT_FOUND));
//            AttachmentValidator.isMe(1L,attachment.getUser().getId());
//            Attachment findAttachment = new Attachment(
//                    attachment.getId(),
//                    attachment.getUser(),
//                    attachment.getTargetId(),
//                    attachment.getAttachment_absolute_path(),
//                    attachment.getAttachment_relative_path(),
//                    attachment.getAttachment_original_name(),
//                    attachment.getAttachment_size(),
//                    attachment.getTarget()
//            );
//            attachments.add(findAttachment);
//        }
//        System.out.println("!!!!!!!!!!!!!!!!!!!! 쿼리 끝 !!!!!!!!!!!!!!!!!!!!!!");
//        Long end = System.currentTimeMillis();
//        System.out.println("----------------------------------------------------------------");
//        System.out.println(end - start);
//        System.out.println("----------------------------------------------------------------");
//    }

//    @Transactional
//    @Test
//    @DisplayName("쿼리를 사용하되, 반복문으로 파일들 가져오기")
//    void test2(){
//        Long start = System.currentTimeMillis();
//        List<Attachment> attachments = new ArrayList<>();
//        System.out.println("!!!!!!!!!!!!!!!!!!!! 쿼리 시작 !!!!!!!!!!!!!!!!!!!!!!");
//        for ( Long fileId : fileIds ) {
//            Attachment attachment = attachmentRepository.findByFileIsMe(1L, fileId);
//            attachments.add(attachment);
//        }
//        System.out.println("!!!!!!!!!!!!!!!!!!!! 쿼리 끝 !!!!!!!!!!!!!!!!!!!!!!");
//        Long end = System.currentTimeMillis();
//        System.out.println("----------------------------------------------------------------");
//        System.out.println(end - start);
//        System.out.println("----------------------------------------------------------------");
//
//    }

//    @Transactional
//    @Test
//    @DisplayName("온전한 쿼리를 사용하여 거져오기")
//    void test3(){
//        Long start = System.currentTimeMillis();
//        System.out.println("!!!!!!!!!!!!!!!!!!!! 쿼리 시작 !!!!!!!!!!!!!!!!!!!!!!");
//        List<Attachment> allByFileIsMe = attachmentRepository.findAllByAttachmentIsMe(1L, fileIds);
//        System.out.println("!!!!!!!!!!!!!!!!!!!! 쿼리 끝 !!!!!!!!!!!!!!!!!!!!!!");
//        Long end = System.currentTimeMillis();
//        System.out.println("----------------------------------------------------------------");
//        System.out.println(end - start);
//        System.out.println("----------------------------------------------------------------");
//    }

}
