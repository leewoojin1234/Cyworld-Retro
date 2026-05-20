package kr.hs.dgsw.cyworldretro.domain.diary;

import kr.hs.dgsw.cyworldretro.domain.diary.dto.DiaryCreateRequest;
import kr.hs.dgsw.cyworldretro.domain.diary.dto.DiaryUpdateRequest;
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
class DiaryServiceTest {

    @Mock
    private DiaryRepository diaryRepository;

    @Mock
    private MiniHomeRepository miniHomeRepository;

    @Mock
    private MemberRepository memberRepository;

    private DiaryService diaryService;

    @BeforeEach
    void setUp() {
        diaryService = new DiaryService(diaryRepository, miniHomeRepository, memberRepository);
    }

    @Test
    @DisplayName("내 미니홈피에 다이어리를 작성한다")
    void create_shouldSaveDiaryOnMyMiniHome() {
        // given
        Member owner = member(1L, "owner@test.com", "주인");
        MiniHome miniHome = miniHome(1L, owner);

        when(memberRepository.findByEmail("owner@test.com")).thenReturn(Optional.of(owner));
        when(miniHomeRepository.findByMember(owner)).thenReturn(Optional.of(miniHome));
        when(diaryRepository.save(any(Diary.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        var response = diaryService.create(
                DiaryCreateRequest.builder()
                        .title("오늘")
                        .content("좋은 날")
                        .emotion(Emotion.HAPPY)
                        .publicPost(true)
                        .build(),
                "owner@test.com"
        );

        // then
        assertThat(response.getTitle()).isEqualTo("오늘");
        assertThat(response.getEmotion()).isEqualTo(Emotion.HAPPY);
        assertThat(response.isPublicPost()).isTrue();
    }

    @Test
    @DisplayName("비공개 다이어리는 주인이 아니면 조회할 수 없다")
    void getDiary_shouldFailWhenPrivateDiaryAndViewerIsNotOwner() {
        // given
        Diary diary = diary(1L, member(1L, "owner@test.com", "주인"), false);
        when(diaryRepository.findById(1L)).thenReturn(Optional.of(diary));

        // when & then
        assertThatThrownBy(() -> diaryService.getDiary(1L, "viewer@test.com"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("비공개 다이어리 조회 권한이 없습니다.");
    }

    @Test
    @DisplayName("주인은 다이어리를 수정하고 삭제할 수 있다")
    void updateAndDelete_shouldAllowOwner() {
        // given
        Diary diary = diary(1L, member(1L, "owner@test.com", "주인"), true);
        when(diaryRepository.findById(1L)).thenReturn(Optional.of(diary));

        // when
        var response = diaryService.update(
                1L,
                DiaryUpdateRequest.builder()
                        .title("수정")
                        .content("수정된 내용")
                        .emotion(Emotion.CALM)
                        .publicPost(false)
                        .build(),
                "owner@test.com"
        );
        diaryService.delete(1L, "owner@test.com");

        // then
        assertThat(response.getTitle()).isEqualTo("수정");
        assertThat(response.isPublicPost()).isFalse();
        verify(diaryRepository).delete(diary);
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

    private Diary diary(Long id, Member owner, boolean publicPost) {
        return Diary.builder()
                .id(id)
                .miniHome(miniHome(1L, owner))
                .title("제목")
                .content("내용")
                .emotion(Emotion.HAPPY)
                .isPublic(publicPost)
                .build();
    }
}
