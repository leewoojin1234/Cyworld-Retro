package kr.hs.dgsw.cyworldretro.domain.guestbook;

import kr.hs.dgsw.cyworldretro.domain.guestbook.dto.GuestBookCreateRequest;
import kr.hs.dgsw.cyworldretro.domain.guestbook.dto.GuestBookUpdateRequest;
import kr.hs.dgsw.cyworldretro.domain.member.Member;
import kr.hs.dgsw.cyworldretro.domain.member.MemberRepository;
import kr.hs.dgsw.cyworldretro.domain.minihome.MiniHome;
import kr.hs.dgsw.cyworldretro.domain.minihome.MiniHomeRepository;
import kr.hs.dgsw.cyworldretro.global.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GuestBookServiceTest {

    @Mock
    private GuestBookRepository guestBookRepository;

    @Mock
    private MiniHomeRepository miniHomeRepository;

    @Mock
    private MemberRepository memberRepository;

    private GuestBookService guestBookService;

    @BeforeEach
    void setUp() {
        guestBookService = new GuestBookService(guestBookRepository, miniHomeRepository, memberRepository);
    }

    @Test
    @DisplayName("방명록을 작성한다")
    void create_shouldSaveGuestBook() {
        // given
        Member owner = member(1L, "owner@test.com", "주인");
        Member writer = member(2L, "writer@test.com", "작성자");
        MiniHome miniHome = miniHome(1L, owner);

        when(miniHomeRepository.findById(1L)).thenReturn(Optional.of(miniHome));
        when(memberRepository.findByEmail("writer@test.com")).thenReturn(Optional.of(writer));
        when(guestBookRepository.save(any(GuestBook.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        var response = guestBookService.create(
                1L,
                GuestBookCreateRequest.builder().content("안녕").secret(true).build(),
                "writer@test.com"
        );

        // then
        assertThat(response.getContent()).isEqualTo("안녕");
        assertThat(response.isSecret()).isTrue();
        assertThat(response.getWriterNickname()).isEqualTo("작성자");
    }

    @Test
    @DisplayName("작성자가 아니면 방명록을 수정할 수 없다")
    void update_shouldFailWhenRequesterIsNotWriter() {
        // given
        GuestBook guestBook = guestBook(1L, member(1L, "owner@test.com", "주인"), member(2L, "writer@test.com", "작성자"));
        when(guestBookRepository.findById(1L)).thenReturn(Optional.of(guestBook));

        // when & then
        assertThatThrownBy(() -> guestBookService.update(
                1L,
                GuestBookUpdateRequest.builder().content("수정").secret(false).build(),
                "other@test.com"
        ))
                .isInstanceOf(BusinessException.class)
                .hasMessage("방명록 수정 권한이 없습니다.");
    }

    @Test
    @DisplayName("미니홈피 주인은 방명록을 삭제할 수 있다")
    void delete_shouldAllowMiniHomeOwner() {
        // given
        GuestBook guestBook = guestBook(1L, member(1L, "owner@test.com", "주인"), member(2L, "writer@test.com", "작성자"));
        when(guestBookRepository.findById(1L)).thenReturn(Optional.of(guestBook));

        // when
        guestBookService.delete(1L, "owner@test.com");

        // then
        verify(guestBookRepository).delete(guestBook);
    }

    private Member member(Long id, String email, String nickname) {
        return Member.builder()
                .id(id)
                .email(email)
                .password("password")
                .nickname(nickname)
                .build();
    }

    private MiniHome miniHome(Long id, Member owner) {
        return MiniHome.builder()
                .id(id)
                .member(owner)
                .build();
    }

    private GuestBook guestBook(Long id, Member owner, Member writer) {
        return GuestBook.builder()
                .id(id)
                .miniHome(miniHome(1L, owner))
                .writer(writer)
                .content("내용")
                .isSecret(false)
                .build();
    }
}
