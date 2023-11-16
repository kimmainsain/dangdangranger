package com.shield.dangdangranger.global.util.jwt;

import static com.shield.dangdangranger.global.util.jwt.constant.TokenSubject.ACCESS_TOKEN;
import static com.shield.dangdangranger.global.util.jwt.constant.TokenSubject.REFRESH_TOKEN;
import static com.shield.dangdangranger.global.util.oauth.constant.OAuth2ExceptionMessage.EXPIRED_TOKEN;
import static com.shield.dangdangranger.global.util.oauth.constant.OAuth2ExceptionMessage.INVALID_TOKEN;

import com.shield.dangdangranger.domain.user.dto.TokenInfo;
import com.shield.dangdangranger.global.error.TokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenHandler {

    private final Key key;

    @Value("${jwt.access-expired-in}")
    private long ACCESS_TOKEN_EXPIRED_IN;

    @Value("${jwt.refresh-expired-in}")
    private long REFRESH_TOKEN_EXPIRED_IN;

    public JwtTokenHandler(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenInfo generateToken(Integer userNo) {
        log.debug("[generateToken] user no : {}", userNo);
        long currentTime = (new Date()).getTime();

        Date accessTokenExpiresIn = new Date(currentTime + ACCESS_TOKEN_EXPIRED_IN);
        String accessToken = Jwts.builder()
            .setSubject(ACCESS_TOKEN.message())
            .claim("user-no", userNo)
            .setExpiration(accessTokenExpiresIn)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

        // ### 디버깅 용
        Claims claims = parseClaims(accessToken);
        log.debug("[DEBUG/getUserNoFromToken] claims : {}", claims);
        // 디버깅 용 ###

        Date refreshTokenExpiresIn = new Date(currentTime + REFRESH_TOKEN_EXPIRED_IN);
        String refreshToken = Jwts.builder()
            .setSubject(REFRESH_TOKEN.message())
            .claim("user-no", userNo)
            .setExpiration(refreshTokenExpiresIn)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

        return TokenInfo.builder()
            .grantType("Bearer")
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

    public boolean validateToken(String token) {
        log.debug("[jwtTokenProvider - validateToken] token : {}", token);

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException |
                 IllegalArgumentException e) {
            e.printStackTrace();
            throw new InvalidTokenException(INVALID_TOKEN.message());
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            throw new ExpiredTokenException(EXPIRED_TOKEN.message());
        }
    }

    public Integer getUserNoFromToken(String token) {
        Claims claims = parseClaims(token);
        log.debug("[DEBUG/getFUserNoFromToken] claims : {}", claims);

        if (claims.get("user-no") == null) {
            log.debug("[JwtTokenProvider - getUserNoFromToken] user no 가 Null!!!!");
            throw new TokenException(INVALID_TOKEN.message());
        }

        return claims.get("user-no", Integer.class);
    }

    public String getTokenSubject(String token) {
        Claims claims = parseClaims(token);
        return claims.getSubject();
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();
        } catch (ExpiredJwtException e) {
            throw new TokenException(EXPIRED_TOKEN.message());
        }
    }
}
