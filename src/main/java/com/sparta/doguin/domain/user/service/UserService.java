package com.sparta.doguin.domain.user.service;

import com.sparta.doguin.config.security.AuthUser;
import com.sparta.doguin.domain.common.exception.UserException;
import com.sparta.doguin.domain.common.response.ApiResponse;
import com.sparta.doguin.domain.common.response.ApiResponseUserEnum;
import com.sparta.doguin.domain.user.dto.UserRequest;
import com.sparta.doguin.domain.user.dto.UserResponse;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 로그인한 사용자의 회원 정보를 조회하는 메서드
     *
     * @param authUser  로그인한 사용자의 인증 정보
     * @return  ApiResponse<UserResponse.Check> 조회된 회원 정보와 성공 메시지를 포함한 응답 객체
     * @throws UserException  사용자가 존재하지 않을 경우 예외 발생
     * @since 1.0
     * @author 황윤서
     */
    public ApiResponse<UserResponse.Check> checkUser(AuthUser authUser) {
        User user = findById(authUser.getUserId());

        UserResponse.Check checkUser = new UserResponse.Check(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getUserType(),
                user.getUserRole(),
                user.getProfileImage(),
                user.getIntroduce(),
                user.getHomeAddress(),
                user.getGitAddress(),
                user.getBlogAddress(),
                user.getUserGrade()
        );

        return ApiResponse.of(ApiResponseUserEnum.USER_CHECK_SUCCESS, checkUser);
    }

    /**
     * 회원 정보를 수정하는 메서드
     *
     * @param authUser    로그인한 사용자의 인증 정보
     * @param userRequest 사용자가 요청한 수정할 회원 정보
     * @return ApiResponse<UserResponse.Update> 수정된 회원 정보와 성공 메시지를 포함한 응답 객체
     * @throws UserException 사용자가 존재하지 않을 경우 예외 발생
     * @since 1.0
     * @author 황윤서
     */
    @Transactional
    public ApiResponse<UserResponse.Update> updateUser(AuthUser authUser, UserRequest.Update userRequest) {
        User user = findById(authUser.getUserId());

        user.update(userRequest);

        // 비밀번호가 수정되는 경우에 필요한 비밀번호 암호화 처리후 암호화된 비밀번호로 업데이트
        if (userRequest.password() != null && !userRequest.password().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(userRequest.password());
            user.updatePassword(encodedPassword);
        }

        UserResponse.Update updatedUser = new UserResponse.Update(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getUserType(),
                user.getUserRole(),
                user.getProfileImage(),
                user.getIntroduce(),
                user.getHomeAddress(),
                user.getGitAddress(),
                user.getBlogAddress()
        );
        return ApiResponse.of(ApiResponseUserEnum.USER_UPDATE_SUCCESS, updatedUser);
    }

    /**
     * 로그인한 사용자의 회원 탈퇴하는 메서드
     *
     * @param authUser  로그인한 사용자의 인증 정보
     * @return  ApiResponse<Void> 성공적으로 회원 정보가 삭제되었음을 알리는 응답
     * @throws UserException  사용자가 존재하지 않을 경우 예외 발생
     * @since 1.0
     * @author 황윤서
     */
    @Transactional
    public ApiResponse<Void> deleteUser(AuthUser authUser) {
        User user = findById(authUser.getUserId());
        userRepository.delete(user);
        return ApiResponse.of(ApiResponseUserEnum.USER_DELETE_SUCCESS);
    }

    /**
     * 유저 ID로 유저를 찾는 메서드
     *
     * @param userId 찾고자 하는 유저의 ID
     * @return User 찾은 유저 객체
     * @throws UserException 유저를 찾지 못한 경우 예외 처리
     * @since 1.0
     * @author 황윤서
     */
    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserException(ApiResponseUserEnum.USER_NOT_FOUND));
    }

    /**
     * 이메일로 유저를 찾는 메서드
     *
     * @param email 찾고자 하는 유저의 이메일
     * @return User 찾은 유저 객체
     * @throws UserException 유저를 찾지 못한 경우 예외 처리
     * @since 1.0
     * @author 황윤서
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ApiResponseUserEnum.USER_NOT_FOUND));
    }
}