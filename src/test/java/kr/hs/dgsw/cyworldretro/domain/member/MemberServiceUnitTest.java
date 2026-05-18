package kr.hs.dgsw.cyworldretro.domain.member;

import kr.hs.dgsw.cyworldretro.domain.member.dto.MemberInfoResponse;
import kr.hs.dgsw.cyworldretro.domain.member.dto.MemberLoginRequest;
import kr.hs.dgsw.cyworldretro.domain.member.dto.ReissueTokenRequest;
import kr.hs.dgsw.cyworldretro.domain.member.dto.TokenResponse;
import kr.hs.dgsw.cyworldretro.domain.minihome.MiniHomeRepository;
import kr.hs.dgsw.cyworldretro.global.exception.BusinessException;
import kr.hs.dgsw.cyworldretro.global.security.JwtTokenProvider;
import kr.hs.dgsw.cyworldretro.global.security.RefreshTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceUnitTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MiniHomeRepository miniHomeRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private RefreshTokenService refreshTokenService;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberService = new MemberService(
                memberRepository,
                miniHomeRepository,
                passwordEncoder,
                jwtTokenProvider,
                refreshTokenService
        );
    }

    @Test
    @DisplayName("로그인 성공 시 Access Token과 Refresh Token을 발급하고 Refresh Token을 저장한다")
    void login_shouldIssueTokensAndSaveRefreshToken() {
        // given
        String email = "test@test.com";
        Member member = Member.builder()
                .email(email)
                .password(passwordEncoder.encode("password"))
                .nickname("테스트")
                .build();

        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member));
        when(jwtTokenProvider.createAccessToken(email)).thenReturn("access-token");
        when(jwtTokenProvider.createRefreshToken(email)).thenReturn("refresh-token");

        // when
        TokenResponse response = memberService.login(MemberLoginRequest.builder()
                .email(email)
                .password("password")
                .build());

        // then
        assertThat(response.getAccessToken()).isEqualTo("access-token");
        assertThat(response.getRefreshToken()).isEqualTo("refresh-token");
        verify(refreshTokenService).saveRefreshToken(email, "refresh-token");
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않으면 로그인에 실패한다")
    void login_shouldFailWhenPasswordDoesNotMatch() {
        // given
        String email = "test@test.com";
        Member member = Member.builder()
                .email(email)
                .password(passwordEncoder.encode("password"))
                .nickname("테스트")
                .build();

        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member));

        // when & then
        assertThatThrownBy(() -> memberService.login(MemberLoginRequest.builder()
                .email(email)
                .password("wrong-password")
                .build()))
                .isInstanceOf(BusinessException.class)
                .hasMessage("이메일 또는 비밀번호가 일치하지 않습니다.");
    }

    @Test
    @DisplayName("Refresh Token이 Redis에 저장된 값과 일치하면 토큰을 재발급한다")
    void reissue_shouldIssueNewTokensWhenRefreshTokenMatches() {
        // given
        String email = "test@test.com";
        String oldRefreshToken = "old-refresh-token";

        when(jwtTokenProvider.validateToken(oldRefreshToken)).thenReturn(true);
        when(jwtTokenProvider.getEmail(oldRefreshToken)).thenReturn(email);
        when(refreshTokenService.getRefreshToken(email)).thenReturn(oldRefreshToken);
        when(jwtTokenProvider.createAccessToken(email)).thenReturn("new-access-token");
        when(jwtTokenProvider.createRefreshToken(email)).thenReturn("new-refresh-token");

        // when
        TokenResponse response = memberService.reissue(ReissueTokenRequest.builder()
                .refreshToken(oldRefreshToken)
                .build());

        // then
        assertThat(response.getAccessToken()).isEqualTo("new-access-token");
        assertThat(response.getRefreshToken()).isEqualTo("new-refresh-token");
        verify(refreshTokenService).saveRefreshToken(email, "new-refresh-token");
    }

    @Test
    @DisplayName("Refresh Token이 Redis에 저장된 값과 다르면 재발급에 실패한다")
    void reissue_shouldFailWhenRefreshTokenDoesNotMatch() {
        // given
        String email = "test@test.com";
        String requestRefreshToken = "request-refresh-token";

        when(jwtTokenProvider.validateToken(requestRefreshToken)).thenReturn(true);
        when(jwtTokenProvider.getEmail(requestRefreshToken)).thenReturn(email);
        when(refreshTokenService.getRefreshToken(email)).thenReturn("saved-refresh-token");

        // when & then
        assertThatThrownBy(() -> memberService.reissue(ReissueTokenRequest.builder()
                .refreshToken(requestRefreshToken)
                .build()))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Refresh Token이 일치하지 않습니다.");
    }

    @Test
    @DisplayName("로그아웃 시 저장된 Refresh Token을 삭제한다")
    void logout_shouldDeleteRefreshToken() {
        // given
        String email = "test@test.com";

        // when
        memberService.logout(email);

        // then
        verify(refreshTokenService).deleteRefreshToken(email);
    }

    @Test
    @DisplayName("인증된 사용자의 내 정보를 조회한다")
    void getMyInfo_shouldReturnMemberInfo() {
        // given
        String email = "test@test.com";
        Member member = Member.builder()
                .id(1L)
                .email(email)
                .password("encoded-password")
                .nickname("테스트")
                .acorn(10)
                .build();

        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member));

        // when
        MemberInfoResponse response = memberService.getMyInfo(email);

        // then
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getEmail()).isEqualTo(email);
        assertThat(response.getNickname()).isEqualTo("테스트");
        assertThat(response.getAcorn()).isEqualTo(10);
    }
}
