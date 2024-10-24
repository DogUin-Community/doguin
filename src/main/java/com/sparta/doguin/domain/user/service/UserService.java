package com.sparta.doguin.domain.user.service;

import com.sparta.doguin.domain.common.exception.UserException;
import com.sparta.doguin.domain.common.response.ApiResponseUserEnum;
import com.sparta.doguin.domain.user.entity.User;
import com.sparta.doguin.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    /**
     * 유저 ID로 유저를 찾는 메서드
     *
     * @param userId 찾고자 하는 유저의 ID
     * @return User 찾은 유저 객체
     * @throws UserException 유저를 찾지 못한 경우 예외 처리
     * @since 1.0
     * @author 황윤서
     */
    public User findById(Long userId){
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