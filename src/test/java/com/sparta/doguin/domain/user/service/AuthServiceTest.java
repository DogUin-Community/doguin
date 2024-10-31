package com.sparta.doguin.domain.user.service;

import com.sparta.doguin.domain.attachment.service.interfaces.AttachmentUploadService;
import com.sparta.doguin.domain.common.exception.UserException;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseUserEnum;
import com.sparta.doguin.domain.setup.DataUtil;
import com.sparta.doguin.domain.user.dto.UserRequest;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.domain.user.repository.UserRepository;
import com.sparta.doguin.security.JwtUtil;
import com.sparta.doguin.security.dto.JwtUtilRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JwtUtil jwtUtil;

    @Mock
    HttpServletResponse response;

    @Mock
    AttachmentUploadService attachmentUploadService;

    @InjectMocks
    AuthService authService;

    User user1;

    @BeforeEach
    void setUp() {
        user1 = DataUtil.user1();
    }

    @Nested
    class SignupTest {
        //TODO: 여기 다시 바꿔야함
//        @Test
//        @DisplayName("회원가입 성공")
//        void signup_success() {
//            // given
//            UserRequest.Signup signupRequest = new UserRequest.Signup(
//                    user1.getEmail(),
//                    "newPassword",
//                    user1.getNickname(),
//                    user1.getUserType().name(),
//                    user1.getUserRole().name(),
//                    user1.getProfileImage(),
//                    user1.getIntroduce(),
//                    user1.getHomeAddress(),
//                    user1.getGitAddress(),
//                    user1.getBlogAddress()
//            );
//
//            List<MultipartFile> files = new ArrayList<>();
//            MultipartFile mockFile = Mockito.mock(MultipartFile.class);
//            files.add(mockFile);
//
//            given(userRepository.findByEmail(user1.getEmail())).willReturn(Optional.empty());
//            given(passwordEncoder.encode("newPassword")).willReturn("encodedPassword");
//            given(userRepository.save(Mockito.any(User.class))).willReturn(user1);
//
//            // when
//            ApiResponse<String> actual = authService.signup(signupRequest, files);
//
//            // then
//            assertEquals(ApiResponseUserEnum.USER_CREATE_SUCCESS.getCode(), actual.getCode());
//            assertEquals(ApiResponseUserEnum.USER_CREATE_SUCCESS.getMessage(), actual.getMessage());
//            assertEquals(null, actual.getData());
//            Mockito.verify(attachmentUploadService).upload(
//                    ArgumentMatchers.anyList(),
//                    ArgumentMatchers.any(),
//                    ArgumentMatchers.anyLong(),
//                    ArgumentMatchers.eq(AttachmentTargetType.PORTFOLIO)
//            );
//        }

        @Test
        @DisplayName("회원가입 실패 - 중복된 이메일")
        void signup_duplicateEmail() {
            // given
            UserRequest.Signup signupRequest = new UserRequest.Signup(
                    user1.getEmail(),
                    "newPassword",
                    user1.getNickname(),
                    user1.getUserType().name(),
                    user1.getUserRole().name(),
                    user1.getProfileImage(),
                    user1.getIntroduce(),
                    user1.getHomeAddress(),
                    user1.getGitAddress(),
                    user1.getBlogAddress()
            );
            given(userRepository.findByEmail(user1.getEmail())).willReturn(Optional.of(user1));

            // when & then
            assertThrows(UserException.class, () -> authService.signup(signupRequest, null));
        }
    }

    @Nested
    class SigninTest {
        @Test
        @DisplayName("로그인 성공")
        void signin_success() {
            // given
            UserRequest.Signin signinRequest = new UserRequest.Signin(user1.getEmail(), "password123");
            given(userRepository.findByEmail(user1.getEmail())).willReturn(Optional.of(user1));
            given(passwordEncoder.matches("password123", user1.getPassword())).willReturn(true);

            JwtUtilRequest.CreateToken createToken = new JwtUtilRequest.CreateToken(
                    user1.getId(),
                    user1.getEmail(),
                    user1.getNickname(),
                    user1.getUserType(),
                    user1.getUserRole()
            );
            String expectedToken = "jwt_token";
            given(jwtUtil.createToken(createToken)).willReturn(expectedToken);

            // when
            ApiResponse<String> actual = authService.signin(signinRequest, response);

            // then
            assertEquals(ApiResponseUserEnum.USER_LOGIN_SUCCESS.getCode(), actual.getCode());
            assertEquals(ApiResponseUserEnum.USER_LOGIN_SUCCESS.getMessage(), actual.getMessage());
            assertEquals(null, actual.getData());
            Mockito.verify(jwtUtil).addTokenToResponseHeader(expectedToken, response); // JWT가 헤더에 추가되었는지 확인
        }

        @Test
        @DisplayName("로그인 실패 - 존재하지 않는 사용자")
        void signin_userNotFound() {
            // given
            UserRequest.Signin signinRequest = new UserRequest.Signin(user1.getEmail(), "password123");
            given(userRepository.findByEmail(user1.getEmail())).willReturn(Optional.empty());

            // when & then
            assertThrows(UserException.class, () -> authService.signin(signinRequest, response));
        }

        @Test
        @DisplayName("로그인 실패 - 비밀번호 불일치")
        void signin_invalidPassword() {
            // given
            UserRequest.Signin signinRequest = new UserRequest.Signin(user1.getEmail(), "wrongPassword");
            given(userRepository.findByEmail(user1.getEmail())).willReturn(Optional.of(user1));
            given(passwordEncoder.matches("wrongPassword", user1.getPassword())).willReturn(false);

            // when & then
            assertThrows(UserException.class, () -> authService.signin(signinRequest, response));
        }
    }
}
