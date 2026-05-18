package kr.hs.dgsw.cyworldretro.domain.member;

import kr.hs.dgsw.cyworldretro.domain.member.dto.MemberJoinRequest;
import kr.hs.dgsw.cyworldretro.domain.member.dto.MemberInfoResponse;
import kr.hs.dgsw.cyworldretro.domain.member.dto.MemberLoginRequest;
import kr.hs.dgsw.cyworldretro.domain.member.dto.ReissueTokenRequest;
import kr.hs.dgsw.cyworldretro.domain.member.dto.TokenResponse;
import kr.hs.dgsw.cyworldretro.domain.minihome.MiniHome;
import kr.hs.dgsw.cyworldretro.domain.minihome.MiniHomeRepository;
import kr.hs.dgsw.cyworldretro.global.exception.BusinessException;
import kr.hs.dgsw.cyworldretro.global.security.JwtTokenProvider;
import kr.hs.dgsw.cyworldretro.global.security.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final MiniHomeRepository miniHomeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void join(MemberJoinRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다.");
        }

        Member member = Member.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .build();

        memberRepository.save(member);

        MiniHome miniHome = MiniHome.builder()
                .member(member)
                .build();

        miniHomeRepository.save(miniHome);
    }

    @Transactional
    public TokenResponse login(MemberLoginRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 일치하지 않습니다."));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        TokenResponse response = issueTokens(member.getEmail());
        refreshTokenService.saveRefreshToken(member.getEmail(), response.getRefreshToken());

        return response;
    }

    public TokenResponse reissue(ReissueTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "유효하지 않은 Refresh Token입니다.");
        }

        String email = jwtTokenProvider.getEmail(refreshToken);
        String savedRefreshToken = refreshTokenService.getRefreshToken(email);

        if (!Objects.equals(savedRefreshToken, refreshToken)) {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "Refresh Token이 일치하지 않습니다.");
        }

        TokenResponse response = issueTokens(email);
        refreshTokenService.saveRefreshToken(email, response.getRefreshToken());

        return response;
    }

    public void logout(String email) {
        refreshTokenService.deleteRefreshToken(email);
    }

    public MemberInfoResponse getMyInfo(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        return MemberInfoResponse.from(member);
    }

    private TokenResponse issueTokens(String email) {
        return TokenResponse.builder()
                .accessToken(jwtTokenProvider.createAccessToken(email))
                .refreshToken(jwtTokenProvider.createRefreshToken(email))
                .build();
    }
}
