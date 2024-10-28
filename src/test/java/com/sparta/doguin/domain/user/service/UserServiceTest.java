package com.sparta.doguin.domain.user.service;

import com.sparta.doguin.config.security.AuthUser;
import com.sparta.doguin.domain.common.exception.UserException;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.user.dto.UserRequest;
import com.sparta.doguin.domain.user.dto.UserResponse;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.domain.user.repository.UserRepository;
import com.sparta.doguin.domain.setup.DataUtil;
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
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    AuthUser authUser1;
    User user1;

    @BeforeEach
    void setUp() {
        authUser1 = DataUtil.authUser1();
        user1 = DataUtil.user1();
    }

    @Nested
    class CheckUserTest {
        @Test
        @DisplayName("회원 정보 조회 성공")
        void checkUser_success() {
            // given
            given(userRepository.findById(user1.getId())).willReturn(Optional.of(user1));

            // when
            ApiResponse<UserResponse.Check> actual = userService.checkUser(authUser1);

            // then
            UserResponse.Check actualData = actual.getData();
            assertEquals(user1.getId(), actualData.id());
            assertEquals(user1.getEmail(), actualData.email());
            assertEquals(user1.getNickname(), actualData.nickname());
        }

        @Test
        @DisplayName("회원 정보 조회 실패 - 유저 존재하지 않음")
        void checkUser_userNotFound() {
            // given
            given(userRepository.findById(user1.getId())).willReturn(Optional.empty());

            // when & then
            assertThrows(UserException.class, () -> userService.checkUser(authUser1));
        }
    }

    @Nested
    class UpdateUserTest {
        @Test
        @DisplayName("회원 정보 수정 성공")
        void updateUser_success() {
            // given
            UserRequest.Update userRequest = new UserRequest.Update(
                    "test@naver.com",
                    "newPassword1!",
                    "newNickname",
                    "newProfileImage",
                    "newIntroduce",
                    "newHomeAddress",
                    "newGitAddress",
                    "newBlogAddress");
            given(userRepository.findById(user1.getId())).willReturn(Optional.of(user1));
            given(passwordEncoder.encode("newPassword1!")).willReturn("encodedPassword");

            // when
            ApiResponse<UserResponse.Update> actual = userService.updateUser(authUser1, userRequest);

            // then
            UserResponse.Update actualData = actual.getData();
            assertEquals("newNickname", actualData.nickname());
            assertEquals("encodedPassword", user1.getPassword());
        }

        @Test
        @DisplayName("회원 정보 수정 실패 - 유저 존재하지 않음")
        void updateUser_userNotFound() {
            // given
            UserRequest.Update userRequest = new UserRequest.Update(
                    "test@naver.com",
                    "newPassword1!",
                    "newNickname",
                    "newProfileImage",
                    "newIntroduce",
                    "newHomeAddress",
                    "newGitAddress",
                    "newBlogAddress");
            given(userRepository.findById(user1.getId())).willReturn(Optional.empty());

            // when & then
            assertThrows(UserException.class, () -> userService.updateUser(authUser1, userRequest));
        }
    }

    @Nested
    class DeleteUserTest {
        @Test
        @DisplayName("회원 탈퇴 성공")
        void deleteUser_success() {
            // given
            given(userRepository.findById(user1.getId())).willReturn(Optional.of(user1));

            // when
            userService.deleteUser(authUser1);

            // then
            Mockito.verify(userRepository, Mockito.times(1)).delete(user1);
        }

        @Test
        @DisplayName("회원 탈퇴 실패 - 유저 존재하지 않음")
        void deleteUser_userNotFound() {
            // given
            given(userRepository.findById(user1.getId())).willReturn(Optional.empty());

            // when & then
            assertThrows(UserException.class, () -> userService.deleteUser(authUser1));
        }
    }

    @Nested
    class FindByIdTest {
        @Test
        @DisplayName("ID로 유저 조회 성공")
        void findById_success() {
            // given
            given(userRepository.findById(user1.getId())).willReturn(Optional.of(user1));

            // when
            User actual = userService.findById(user1.getId());

            // then
            assertEquals(user1.getId(), actual.getId());
            assertEquals(user1.getEmail(), actual.getEmail());
        }

        @Test
        @DisplayName("ID로 유저 조회 실패 - 유저 존재하지 않음")
        void findById_userNotFound() {
            // given
            given(userRepository.findById(user1.getId())).willReturn(Optional.empty());

            // when & then
            assertThrows(UserException.class, () -> userService.findById(user1.getId()));
        }
    }

    @Nested
    class FindByEmailTest {
        @Test
        @DisplayName("이메일로 유저 조회 성공")
        void findByEmail_success() {
            // given
            given(userRepository.findByEmail(user1.getEmail())).willReturn(Optional.of(user1));

            // when
            User actual = userService.findByEmail(user1.getEmail());

            // then
            assertEquals(user1.getId(), actual.getId());
            assertEquals(user1.getEmail(), actual.getEmail());
        }

        @Test
        @DisplayName("이메일로 유저 조회 실패 - 유저 존재하지 않음")
        void findByEmail_userNotFound() {
            // given
            given(userRepository.findByEmail(user1.getEmail())).willReturn(Optional.empty());

            // when & then
            assertThrows(UserException.class, () -> userService.findByEmail(user1.getEmail()));
        }
    }
}
