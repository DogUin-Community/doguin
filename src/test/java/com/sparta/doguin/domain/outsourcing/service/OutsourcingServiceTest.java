package com.sparta.doguin.domain.outsourcing.service;

import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentDeleteService;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentGetService;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentUpdateService;
import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentUploadService;
import com.sparta.doguin.domain.outsourcing.constans.AreaType;
import com.sparta.doguin.domain.outsourcing.entity.Outsourcing;
import com.sparta.doguin.domain.outsourcing.model.OutsourcingRequest;
import com.sparta.doguin.domain.outsourcing.model.OutsourcingResponse;
import com.sparta.doguin.domain.outsourcing.repository.OutsourcingRepository;
import com.sparta.doguin.domain.setup.DataUtil;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.security.AuthUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.sparta.doguin.domain.attachment.constans.AttachmentTargetType.OUTSOURCING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OutsourcingServiceTest {

    @Mock
    OutsourcingRepository outsourcingRepository;

    @Mock
    AttachmentUploadService attachmentUploadService;

    @Mock
    AttachmentUpdateService attachmentUpdateService;

    @Mock
    AttachmentGetService attachmentGetService;

    @Mock
    AttachmentDeleteService attachmentDeleteService;

    @InjectMocks
    OutsourcingServiceImpl outsourcingService;

    Pageable pageable;
    User user1;
    User user2;
    AuthUser authUser1;
    AuthUser authUser2;
    Long outsourcingId1;
    Long outsourcingId2;
    OutsourcingRequest.OutsourcingRequestCreate outsourctingRequestCreate1;
    OutsourcingRequest.OutsourcingRequestCreate outsourctingRequestCreate2;
    OutsourcingRequest.OutsourcingRequestUpdate outsourctingRequestUpdate1;
    OutsourcingRequest.OutsourcingRequestUpdate outsourctingRequestUpdate2;
    Outsourcing outsourcing1;
    Outsourcing outsourcing2;


    @BeforeEach
    void setUp() {
        pageable = DataUtil.pageable();
        outsourcingId1 = DataUtil.one();
        outsourcingId2 = DataUtil.two();
        outsourctingRequestCreate1 = DataUtil.outsourctingRequestCreate1();
        outsourctingRequestCreate2 = DataUtil.outsourctingRequestCreate2();
        outsourctingRequestUpdate1 = DataUtil.outsourctingRequestUpdate1();
        outsourctingRequestUpdate2 = DataUtil.outsourctingRequestUpdate2();
        user1 = DataUtil.user1();
        user2 = DataUtil.user2();
        outsourcing1 = DataUtil.outsourcing1();
        outsourcing2 = DataUtil.outsourcing2();
        authUser1 = DataUtil.authUser1();
        authUser2 = DataUtil.authUser2();
    }

    @Nested
    public class 외주_단일_조회_테스트 {
        @Test
        @DisplayName("외주 단일 조회 성공")
        void test() {
            // given
            given(outsourcingRepository.findById(outsourcingId1)).willReturn(Optional.of(outsourcing1));
            given(attachmentGetService.getAllAttachmentPath(outsourcingId1,OUTSOURCING)).willReturn(List.of("1"));

            // when
            OutsourcingResponse actual = outsourcingService.getOutsourcing(outsourcingId1);
            OutsourcingResponse.OutsourcingResponseGetFilePaths actualData = (OutsourcingResponse.OutsourcingResponseGetFilePaths) actual;


            // then - 외주 아이디에 대한 데이터로, 각각의 값이 일치하는지 확인
            assertEquals( outsourcing1.getUser().getId(), actualData.userId() );
            assertEquals( outsourcing1.getTitle(), actualData.title() );
            assertEquals( outsourcing1.getContent(), actualData.content() );
        }
    }

    @Nested
    public class 외주_생성_테스트 {
        @Test
        @DisplayName("외주 생성 성공")
        void test() {
            // given

            // when
            outsourcingService.createOutsourcing(outsourctingRequestCreate1, authUser1,null);

            // then - 1번 호출 됐는지, 예상한 데이터와 실제 데이터가 일치하는지 검증
            Mockito.verify(outsourcingRepository, Mockito.times(1)).save(Mockito.argThat(outsourcing ->
                    outsourcing.getTitle().equals(outsourcing1.getTitle()) &&
                            outsourcing.getContent().equals(outsourcing1.getContent()) &&
                            outsourcing.getUser().getId().equals(authUser1.getUserId())) // 사용자 확인
            );

        }
    }

    @Nested
    public class 외주_수정_테스트 {
        @Test
        @DisplayName("외주 수정 성공")
        void test() {
            // given
            given(outsourcingRepository.findById(outsourcingId1)).willReturn(Optional.of(outsourcing1));

            // when
            outsourcingService.updateOutsourcing(outsourcingId1, outsourctingRequestUpdate1, authUser1,null);

            // then - 1번 호출 됐는지, 예상한 데이터와 실제 데이터가 일치하는지 검증
            Mockito.verify(outsourcingRepository, Mockito.times(1)).save(Mockito.argThat(outsourcing ->
                    !outsourcing1.getTitle().equals(outsourctingRequestUpdate1.title()) &&
                            !outsourcing1.getContent().equals(outsourctingRequestUpdate1.content()) &&
                            outsourcing.getUser().getId().equals(outsourcing1.getUser().getId()) // 사용자 확인
            ));
        }
    }

    @Nested
    public class 외주_삭제_테스트 {
        @Test
        @DisplayName("외주 삭제 성공")
        void test() {
            // given
            given(outsourcingRepository.findById(outsourcingId1)).willReturn(Optional.of(outsourcing1));
            given(attachmentGetService.getFileIds(outsourcing1.getUser().getId(),outsourcing1.getId(),OUTSOURCING)).willReturn(List.of(1L));

            // when
            outsourcingService.deleteOutsourcing(outsourcingId1,authUser1);

            // then
            Mockito.verify(outsourcingRepository, Mockito.times(1)).delete(Mockito.argThat(outsourcing ->
                    outsourcing.getTitle().equals(outsourcing1.getTitle()) &&
                            outsourcing.getContent().equals(outsourcing1.getContent()) &&
                            outsourcing.getUser().getId().equals(outsourcing1.getUser().getId()) // 사용자 확인
            ));
        }
    }

    @Nested
    public class 외주_다건_조회_테스트 {
        @Test
        @DisplayName("외주 다건 조회 성공 _ 지역 선택 O ")
        void test() {
            // given
            AreaType area = AreaType.SEOUL;
            List<Outsourcing> outsourcings = List.of(outsourcing1, outsourcing2);
            Page<Outsourcing> pageOutsourcing = new PageImpl<>(outsourcings,pageable,outsourcings.size());
            given(outsourcingRepository.findAllByArea(pageable,area)).willReturn(pageOutsourcing);

            // when
            Page<OutsourcingResponse> actual = outsourcingService.getAllOutsourcing(pageable,area);
            List<OutsourcingResponse.OutsourcingResponseGetFilePaths> actualData = actual.getContent().stream()
                    .map(outsourcing -> (OutsourcingResponse.OutsourcingResponseGetFilePaths) outsourcing)
                    .toList();

            // then - 예상한 데이터와, 실제 데이터가 일치하는지 검증
            assertEquals( outsourcings.size(), actualData.size() );
            assertEquals(outsourcing1.getId(), actualData.get(0).id());
            assertEquals(outsourcing1.getUser().getId(), actualData.get(0).userId());
            assertEquals(outsourcing1.getTitle(), actualData.get(0).title());
            assertEquals(outsourcing1.getArea(), actualData.get(0).area());
            assertEquals(outsourcing2.getId(), actualData.get(1).id());
            assertEquals(outsourcing2.getUser().getId(), actualData.get(1).userId());
            assertEquals(outsourcing2.getTitle(), actualData.get(1).title());
            assertEquals(outsourcing2.getArea(), actualData.get(1).area());
        }

        @Test
        @DisplayName("외주 다건 조회 성공 _ 지역 선택 X ")
        void test2() {
            // given
            AreaType area = null;
            List<Outsourcing> outsourcings = List.of(outsourcing1, outsourcing2);
            Page<Outsourcing> pageOutsourcing = new PageImpl<>(outsourcings,pageable,outsourcings.size());
            given(outsourcingRepository.findAllBy(pageable)).willReturn(pageOutsourcing);

            // when
            Page<OutsourcingResponse> actual = outsourcingService.getAllOutsourcing(pageable,area);
            List<OutsourcingResponse.OutsourcingResponseGetFilePaths> actualData = actual.getContent().stream()
                    .map(outsourcing -> (OutsourcingResponse.OutsourcingResponseGetFilePaths) outsourcing)
                    .toList();

            // then - 예상한 데이터와, 실제 데이터가 일치하는지 검증
            assertEquals( outsourcings.size(), actualData.size() );
            assertEquals(outsourcing1.getId(), actualData.get(0).id());
            assertEquals(outsourcing1.getUser().getId(), actualData.get(0).userId());
            assertEquals(outsourcing1.getTitle(), actualData.get(0).title());
            assertEquals(outsourcing1.getArea(), actualData.get(0).area());
            assertEquals(outsourcing2.getId(), actualData.get(1).id());
            assertEquals(outsourcing2.getUser().getId(), actualData.get(1).userId());
            assertEquals(outsourcing2.getTitle(), actualData.get(1).title());
            assertEquals(outsourcing2.getArea(), actualData.get(1).area());
        }
    }
}