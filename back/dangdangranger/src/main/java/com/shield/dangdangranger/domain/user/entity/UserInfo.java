package com.shield.dangdangranger.domain.user.entity;

import java.util.concurrent.TimeUnit;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "user-info")
public class UserInfo {

    @Id
    private Integer userNo;

    private String userId;
    private String userName;
    private String userMessage;
    private String userProfileImg;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long ttl;

    @Builder
    UserInfo(Integer userNo, String userId, String userName, String userMessage,
        String userProfileImg, Long ttl) {
        this.userNo = userNo;
        this.userId = userId;
        this.userName = userName;
        this.userMessage = userMessage;
        this.userProfileImg = userProfileImg;
        this.ttl = ttl;
    }

    public void updateUserInfoMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public void updateUserNameMessage(String userName) {
        this.userName = userName;
    }

}
