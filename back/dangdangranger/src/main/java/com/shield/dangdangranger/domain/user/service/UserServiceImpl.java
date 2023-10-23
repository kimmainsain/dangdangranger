package com.shield.dangdangranger.domain.user.service;


import static com.shield.dangdangranger.domain.user.constant.UserExceptionMessage.USER_NOT_FOUND_EXCEPTION;
import static com.shield.dangdangranger.global.constant.BaseConstant.CANCELED;
import static com.shield.dangdangranger.global.constant.BaseConstant.NOTCANCELED;

import com.shield.dangdangranger.domain.user.dto.TokenInfo;
import com.shield.dangdangranger.domain.user.dto.UserRequestDto.UpdateUserInfoRequestDto;
import com.shield.dangdangranger.domain.user.dto.UserRequestDto.UpdateUserMessageRequestDto;
import com.shield.dangdangranger.domain.user.dto.UserRequestDto.UserInfoRequestDto;
import com.shield.dangdangranger.domain.user.dto.UserResponseDto.AccessTokenResponseDto;
import com.shield.dangdangranger.domain.user.dto.UserResponseDto.UserInfoResponseDto;
import com.shield.dangdangranger.domain.user.entity.User;
import com.shield.dangdangranger.domain.user.entity.UserInfo;
import com.shield.dangdangranger.domain.user.repo.UserRepository;
import com.shield.dangdangranger.global.error.NotFoundException;
import com.shield.dangdangranger.global.util.jwt.JwtTokenHandler;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtTokenHandler jwtTokenHandler;
    private final UserRedisService userRedisService;


    @Override
    public TokenInfo signUpOrIn(UserInfoRequestDto userInfoRequestDto) {
        User user = insertUser(userInfoRequestDto.toUser());
        log.debug("[userServiceImpl - signUpOrIn] User : {}", user);

        userRedisService.insertUserInfoToRedis(user);

        // 토큰 발급 후, 정보 반환
        return jwtTokenHandler.generateToken(user.getUserNo());
    }

    @Transactional
    public User insertUser(User user) {
        log.debug("### [DEBUG/UserService] 회원가입 user : {}", user);

        // DB에 정보 없을 경우 회원가입, 있을 경우 프로필 사진/이름 업데이트
        Optional<User> optionalUser = userRepository.findUserByUserIdAndCanceled(user.getUserId(),
            NOTCANCELED);
        if (optionalUser.isEmpty()) {
            userRepository.save(user);
        }
        return userRepository.findUserByUserIdAndCanceled(user.getUserId(),
            NOTCANCELED).get();
    }

    @Override
    public UserInfoResponseDto getUserInfo(Integer userNo) {
        try {
            // redis 에서 먼저 검색
            log.debug("[UserService] Get user info from redis !!");
            UserInfo userInfo = userRedisService.readUserInfoFromRedis(userNo);
            return UserInfoResponseDto.builder()
                .userId(userInfo.getUserId())
                .userName(userInfo.getUserName())
                .userMessage(userInfo.getUserMessage())
                .userProfileImg(userInfo.getUserProfileImg())
                .build();
        } catch (NotFoundException e) {
            // 없으면 DB 검색
            log.debug("[UserService] Get user info from DB !!");
            User user = userRepository.findUserByUserNoAndCanceled(userNo, NOTCANCELED)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_EXCEPTION.message()));
            
            // redis 에 저장
            userRedisService.insertUserInfoToRedis(user);

            return UserInfoResponseDto.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .userMessage(user.getUserMessage())
                .userProfileImg(user.getUserProfileImg())
                .build();
        }
    }

    @Override
    @Transactional
    public void deleteUser(Integer userNo) {
        // Redis 에서 삭제
        userRedisService.deleteUserInfoFromRedis(userNo);
        
        // DB 에서 삭제
        User user = userRepository.findUserByUserNoAndCanceled(userNo, NOTCANCELED)
            .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_EXCEPTION.message()));

        user.setCanceled(CANCELED);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUserMessage(Integer userNo,
        UpdateUserMessageRequestDto updateUserMessageRequestDto) {
        User user = userRepository.findUserByUserNoAndCanceled(userNo, NOTCANCELED)
            .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_EXCEPTION.message()));

        user.updateUserMessage(updateUserMessageRequestDto.getUserMessage());
        userRepository.save(user);

        // redis 에 적용
        userRedisService.updateUserMessageToRedis(user);
    }

    @Override
    public AccessTokenResponseDto reissueAccessToken(Integer userNo) {
        String accessToken = jwtTokenHandler.generateToken(userNo).getAccessToken();

        return AccessTokenResponseDto.builder()
            .accessToken(accessToken)
            .build();
    }

    @Override
    @Transactional
    public void updateUserName(Integer userNo, UpdateUserInfoRequestDto updateUserInfoRequestDto) {
        User user = userRepository.findUserByUserNoAndCanceled(userNo, NOTCANCELED)
            .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_EXCEPTION.message()));
        log.debug("[userService/updateUserName] update user name : {}",
            updateUserInfoRequestDto.getUserName());

        user.updateUserName(updateUserInfoRequestDto.getUserName());
        userRepository.save(user);

        // redis 에 적용
        userRedisService.updateUserNameToRedis(user);
    }
}
