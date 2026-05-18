package kr.hs.dgsw.cyworldretro.domain.member;

import kr.hs.dgsw.cyworldretro.domain.member.dto.MemberJoinRequest;
import kr.hs.dgsw.cyworldretro.domain.minihome.MiniHome;
import kr.hs.dgsw.cyworldretro.domain.minihome.MiniHomeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MiniHomeRepository miniHomeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입 시 미니홈피가 자동으로 생성되어야 한다")
    void join_shouldCreateMiniHome() {
        // given
        MemberJoinRequest request = MemberJoinRequest.builder()
                .email("test@test.com")
                .password("password")
                .nickname("테스트")
                .build();

        // when
        memberService.join(request);

        // then
        Member member = memberRepository.findByEmail("test@test.com").orElseThrow();
        assertThat(member.getNickname()).isEqualTo("테스트");
        assertThat(passwordEncoder.matches("password", member.getPassword())).isTrue();

        Optional<MiniHome> miniHome = miniHomeRepository.findByMember(member);
        assertThat(miniHome).isPresent();
        assertThat(miniHome.get().getMember().getId()).isEqualTo(member.getId());
    }
}
