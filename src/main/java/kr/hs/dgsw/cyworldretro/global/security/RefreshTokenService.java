package kr.hs.dgsw.cyworldretro.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private static final String REFRESH_TOKEN_PREFIX = "refresh:";

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenValidityInSeconds;

    public void saveRefreshToken(String email, String refreshToken) {
        redisTemplate.opsForValue().set(
                createKey(email),
                refreshToken,
                refreshTokenValidityInSeconds,
                TimeUnit.SECONDS
        );
    }

    public String getRefreshToken(String email) {
        return redisTemplate.opsForValue().get(createKey(email));
    }

    public void deleteRefreshToken(String email) {
        redisTemplate.delete(createKey(email));
    }

    private String createKey(String email) {
        return REFRESH_TOKEN_PREFIX + email;
    }
}
