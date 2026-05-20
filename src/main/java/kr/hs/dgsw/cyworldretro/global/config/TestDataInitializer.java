package kr.hs.dgsw.cyworldretro.global.config;

import kr.hs.dgsw.cyworldretro.domain.acorn.AcornHistory;
import kr.hs.dgsw.cyworldretro.domain.acorn.AcornHistoryRepository;
import kr.hs.dgsw.cyworldretro.domain.acorn.AcornType;
import kr.hs.dgsw.cyworldretro.domain.diary.Diary;
import kr.hs.dgsw.cyworldretro.domain.diary.DiaryRepository;
import kr.hs.dgsw.cyworldretro.domain.diary.Emotion;
import kr.hs.dgsw.cyworldretro.domain.guestbook.GuestBook;
import kr.hs.dgsw.cyworldretro.domain.guestbook.GuestBookRepository;
import kr.hs.dgsw.cyworldretro.domain.ilchon.Ilchon;
import kr.hs.dgsw.cyworldretro.domain.ilchon.IlchonRepository;
import kr.hs.dgsw.cyworldretro.domain.member.Member;
import kr.hs.dgsw.cyworldretro.domain.member.MemberRepository;
import kr.hs.dgsw.cyworldretro.domain.minihome.MiniHome;
import kr.hs.dgsw.cyworldretro.domain.minihome.MiniHomeRepository;
import kr.hs.dgsw.cyworldretro.domain.minihome.SkinType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class TestDataInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final MiniHomeRepository miniHomeRepository;
    private final DiaryRepository diaryRepository;
    private final GuestBookRepository guestBookRepository;
    private final IlchonRepository ilchonRepository;
    private final AcornHistoryRepository acornHistoryRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (memberRepository.count() > 0) {
            return;
        }

        Member owner = createMember("owner@test.com", "홈피주인", 100);
        Member writer = createMember("writer@test.com", "방문자", 50);
        Member friend = createMember("friend@test.com", "일촌친구", 30);

        MiniHome ownerHome = createMiniHome(owner, "오늘도 싸이 감성 충전 중", SkinType.SKY, "/bgm/snow-flower.mp3", 3, 128);
        MiniHome writerHome = createMiniHome(writer, "왔다 가면 방명록 남기기", SkinType.BASIC, null, 1, 42);
        createMiniHome(friend, "일촌 공개 미니홈피", SkinType.PINK, null, 0, 17);

        createDiary(ownerHome, "오늘의 기록", "싸이월드 감성 백엔드 개발 중", Emotion.HAPPY, true);
        createDiary(ownerHome, "비밀 다이어리", "주인만 볼 수 있는 비공개 일기", Emotion.CALM, false);
        createDiary(writerHome, "방문자의 하루", "도토리 충전하고 구경하는 중", Emotion.EXCITED, true);

        createGuestBook(ownerHome, writer, "왔다 갑니다!", false);
        createGuestBook(ownerHome, friend, "비밀 방명록입니다.", true);

        Ilchon ilchon = Ilchon.builder()
                .requester(writer)
                .receiver(owner)
                .build();
        ilchon.accept("베프");
        ilchonRepository.save(ilchon);

        Ilchon pending = Ilchon.builder()
                .requester(friend)
                .receiver(owner)
                .build();
        ilchonRepository.save(pending);

        createHistory(owner, 100, AcornType.CHARGE, "초기 테스트 도토리 충전");
        createHistory(owner, -15, AcornType.PURCHASE, "눈의 꽃 BGM 구매");
    }

    private Member createMember(String email, String nickname, int acorn) {
        Member member = Member.builder()
                .email(email)
                .password(passwordEncoder.encode("password123"))
                .nickname(nickname)
                .acorn(acorn)
                .build();
        return memberRepository.save(member);
    }

    private MiniHome createMiniHome(Member member, String profileMessage, SkinType skinType, String bgmUrl, int todayVisit, int totalVisit) {
        MiniHome miniHome = MiniHome.builder()
                .member(member)
                .profileMessage(profileMessage)
                .skinType(skinType)
                .bgmUrl(bgmUrl)
                .todayVisit(todayVisit)
                .totalVisit(totalVisit)
                .build();
        return miniHomeRepository.save(miniHome);
    }

    private void createDiary(MiniHome miniHome, String title, String content, Emotion emotion, boolean isPublic) {
        Diary diary = Diary.builder()
                .miniHome(miniHome)
                .title(title)
                .content(content)
                .emotion(emotion)
                .isPublic(isPublic)
                .build();
        diaryRepository.save(diary);
    }

    private void createGuestBook(MiniHome miniHome, Member writer, String content, boolean isSecret) {
        GuestBook guestBook = GuestBook.builder()
                .miniHome(miniHome)
                .writer(writer)
                .content(content)
                .isSecret(isSecret)
                .build();
        guestBookRepository.save(guestBook);
    }

    private void createHistory(Member member, int amount, AcornType type, String description) {
        AcornHistory history = AcornHistory.builder()
                .member(member)
                .amount(amount)
                .type(type)
                .description(description)
                .build();
        acornHistoryRepository.save(history);
    }
}
