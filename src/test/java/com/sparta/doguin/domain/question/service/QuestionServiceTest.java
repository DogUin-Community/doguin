package com.sparta.doguin.domain.question.service;

import com.sparta.doguin.domain.answer.repository.AnswerRepository;
import com.sparta.doguin.domain.attachment.constans.AttachmentTargetType;
import com.sparta.doguin.domain.attachment.service.impl.AttachmentDeleteServiceImpl;
import com.sparta.doguin.domain.attachment.service.impl.AttachmentGetServiceImpl;
import com.sparta.doguin.domain.attachment.service.impl.AttachmentUpdateServiceImpl;
import com.sparta.doguin.domain.attachment.service.impl.AttachmentUploadServiceImpl;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.question.dto.QuestionRequest;
import com.sparta.doguin.domain.question.dto.QuestionResponse;
import com.sparta.doguin.domain.question.entity.Question;
import com.sparta.doguin.domain.question.enums.FirstCategory;
import com.sparta.doguin.domain.question.enums.LastCategory;
import com.sparta.doguin.domain.question.enums.SecondCategory;
import com.sparta.doguin.domain.question.repository.QuestionRepository;
import com.sparta.doguin.domain.setup.DataUtil;
import com.sparta.doguin.security.AuthUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private AttachmentUploadServiceImpl attachmentUploadService;

    @Mock
    private AttachmentUpdateServiceImpl attachmentUpdateService;

    @Mock
    private AttachmentGetServiceImpl attachmentGetService;

    @Mock
    private AttachmentDeleteServiceImpl attachmentDeleteServiceImpl;

    @InjectMocks
    private QuestionService questionService;

    @Test
    void 질문_생성_성공() {
        // given
        AuthUser authUser = DataUtil.authUser1();
        QuestionRequest.QuestionRequestCreate request = DataUtil.questionRequestCreate1();
        List<MultipartFile> files = List.of(
                new MockMultipartFile("file1", "test1.jpg", "image/jpeg", "test image content".getBytes()),
                new MockMultipartFile("file2", "test2.jpg", "image/jpeg", "test image content".getBytes())
        );

        Question savedQuestion = new Question(
                DataUtil.one(),
                request.title(),
                request.content(),
                request.firstCategory(),
                request.secondCategory(),
                request.lastCategory(),
                DataUtil.user1()
        );

        List<Long> fileIds = List.of(1L, 2L);

        // when
        given(questionRepository.save(any(Question.class))).willReturn(savedQuestion);
        given(attachmentGetService.getFileIds(authUser.getUserId(), savedQuestion.getId(), AttachmentTargetType.QUESTION))
                .willReturn(fileIds);

        ApiResponse<QuestionResponse> response = questionService.createdQuestion(authUser, request, files);

        // then
        assertEquals("질문 등록에 성공하였습니다.", response.getMessage());

    }

    @Test
    void 질문_수정_성공() {
        // given
        long questionId = DataUtil.one();
        AuthUser authUser = DataUtil.authUser1();
        QuestionRequest.QuestionRequestUpdate request = new QuestionRequest.QuestionRequestUpdate(
                "title", "content", FirstCategory.JAVA, SecondCategory.STRING, LastCategory.REDIS, List.of(1L, 2L)
        );

        List<MultipartFile> files = List.of(
                new MockMultipartFile("file1", "test1.jpg", "image/jpeg", "test image content".getBytes()),
                new MockMultipartFile("file2", "test2.jpg", "image/jpeg", "test image content".getBytes())
        );

        Question question = new Question(
                questionId,
                DataUtil.questionRequestCreate1().title(),
                DataUtil.questionRequestCreate1().content(),
                DataUtil.questionRequestCreate1().firstCategory(),
                DataUtil.questionRequestCreate1().secondCategory(),
                DataUtil.questionRequestCreate1().lastCategory(),
                DataUtil.user1()
        );

        // when
        given(questionRepository.findById(questionId)).willReturn(Optional.of(question));

        ApiResponse<QuestionResponse> response = questionService.updatedQuestion(authUser, questionId, request, files);

        // then
        assertEquals("질문 수정에 성공하셨습니다.", response.getMessage());
    }

    @Test
    void 질문_전체_조회() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Question question = DataUtil.question1();
        Page<Question> questions = new PageImpl<>(List.of(question));

        // when
        given(questionRepository.findAll(pageable)).willReturn(questions);
        ApiResponse<Page<QuestionResponse.GetQuestions>> response = questionService.getQuestions(1, 10);

        // then
        assertEquals("질문 조회(전체)에 성공하였습니다.", response.getMessage());
        assertNotNull(response.getData());
    }

    @Test
    void 질문_삭제_성공() {
        // given
        AuthUser authUser = DataUtil.authUser1();
        long questionId = DataUtil.one();

        Question question = new Question(
                questionId,
                DataUtil.questionRequestCreate1().title(),
                DataUtil.questionRequestCreate1().content(),
                DataUtil.questionRequestCreate1().firstCategory(),
                DataUtil.questionRequestCreate1().secondCategory(),
                DataUtil.questionRequestCreate1().lastCategory(),
                DataUtil.user1()
        );

        // 파일 ID
        List<Long> fileIds = List.of(1L, 2L, 3L);

        // when
        given(questionRepository.findById(questionId)).willReturn(Optional.of(question));
        given(attachmentGetService.getFileIds(authUser.getUserId(), questionId, AttachmentTargetType.QUESTION))
                .willReturn(fileIds);
        ApiResponse<Void> response = questionService.deleteQuestion(authUser, questionId);

        // then
        assertEquals("질문 삭제에 성공하였습니다.", response.getMessage());
    }

}
