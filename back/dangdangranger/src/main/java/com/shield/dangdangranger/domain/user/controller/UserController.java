package com.shield.dangdangranger.domain.user.controller;

import static com.shield.dangdangranger.domain.user.constant.UserResponseMessage.DELETE_USER_SUCCESS;
import static com.shield.dangdangranger.domain.user.constant.UserResponseMessage.GET_USER_INFO_SUCCESS;
import static com.shield.dangdangranger.domain.user.constant.UserResponseMessage.REISSUE_ACCESS_TOKEN__SUCCESS;
import static com.shield.dangdangranger.domain.user.constant.UserResponseMessage.SIGN_IN_SUCCESS;
import static com.shield.dangdangranger.domain.user.constant.UserResponseMessage.UPDATE_USER_MESSAGE_SUCCESS;
import static com.shield.dangdangranger.domain.user.constant.UserResponseMessage.UPDATE_USER_NAME_SUCCESS;

import com.shield.dangdangranger.domain.user.dto.TokenInfo;
import com.shield.dangdangranger.domain.user.dto.UserRequestDto.UpdateUserInfoRequestDto;
import com.shield.dangdangranger.domain.user.dto.UserRequestDto.UpdateUserMessageRequestDto;
import com.shield.dangdangranger.domain.user.dto.UserRequestDto.UserInfoRequestDto;
import com.shield.dangdangranger.domain.user.dto.UserResponseDto.AccessTokenResponseDto;
import com.shield.dangdangranger.domain.user.dto.UserResponseDto.UserInfoResponseDto;
import com.shield.dangdangranger.domain.user.service.UserService;
import com.shield.dangdangranger.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("")
    public ResponseEntity<ResponseDto<TokenInfo>> signUpOrIn(
        @RequestAttribute("userInfo") UserInfoRequestDto userInfoRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(
            ResponseDto.create(SIGN_IN_SUCCESS.message(),
                userService.signUpOrIn(userInfoRequestDto))
        );
    }

    @GetMapping("")
    public ResponseEntity<ResponseDto<UserInfoResponseDto>> getUserInfo(
        @RequestAttribute("userNo") Integer userNo) {
        return ResponseEntity.status(HttpStatus.OK).body(
            ResponseDto.create(GET_USER_INFO_SUCCESS.message(),
                userService.getUserInfo(userNo))
        );
    }

    @DeleteMapping("")
    public ResponseEntity<ResponseDto<String>> deleteUser(
        @RequestAttribute("userNo") Integer userNo) {
        userService.deleteUser(userNo);
        return ResponseEntity.status(HttpStatus.OK).body(
            ResponseDto.create(DELETE_USER_SUCCESS.message())
        );
    }

    @PutMapping("/message")
    public ResponseEntity<ResponseDto<String>> updateUserMessage(
        @RequestAttribute("userNo") Integer userNo,
        @RequestBody UpdateUserMessageRequestDto updateUserMessageRequestDto) {
        userService.updateUserMessage(userNo, updateUserMessageRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(
            ResponseDto.create(UPDATE_USER_MESSAGE_SUCCESS.message())
        );
    }

    @PostMapping("/token")
    public ResponseEntity<ResponseDto<AccessTokenResponseDto>> reissueAccessToken(
        @RequestAttribute("userNo") Integer userNo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ResponseDto.create(REISSUE_ACCESS_TOKEN__SUCCESS.message(),
                userService.reissueAccessToken(userNo))
        );
    }

    @PutMapping("/name")
    public ResponseEntity<ResponseDto<String>> updateUserName(
        @RequestAttribute("userNo") Integer userNo, @RequestBody UpdateUserInfoRequestDto updateUserInfoRequestDto) {
        userService.updateUserName(userNo, updateUserInfoRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(
            ResponseDto.create(UPDATE_USER_NAME_SUCCESS.message())
        );
    }

}
