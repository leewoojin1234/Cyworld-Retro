package kr.hs.dgsw.cyworldretro.domain.diary;

import kr.hs.dgsw.cyworldretro.domain.diary.dto.DiaryCreateRequest;
import kr.hs.dgsw.cyworldretro.domain.diary.dto.DiaryResponse;
import kr.hs.dgsw.cyworldretro.domain.diary.dto.DiaryUpdateRequest;
import kr.hs.dgsw.cyworldretro.domain.member.Member;
import kr.hs.dgsw.cyworldretro.domain.member.MemberRepository;
import kr.hs.dgsw.cyworldretro.domain.minihome.MiniHome;
import kr.hs.dgsw.cyworldretro.domain.minihome.MiniHomeRepository;
import kr.hs.dgsw.cyworldretro.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final MiniHomeRepository miniHomeRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public DiaryResponse create(DiaryCreateRequest request, String ownerEmail) {
        Member owner = getMember(ownerEmail);
        MiniHome miniHome = miniHomeRepository.findByMember(owner)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "미니홈피를 찾을 수 없습니다."));

        Diary diary = Diary.builder()
                .miniHome(miniHome)
                .title(request.getTitle())
                .content(request.getContent())
                .emotion(request.getEmotion())
                .isPublic(request.isPublicPost())
                .build();

        return DiaryResponse.from(diaryRepository.save(diary));
    }

    public Page<DiaryResponse> getDiaries(Long miniHomeId, String keyword, Emotion emotion, String viewerEmail, Pageable pageable) {
        getMiniHome(miniHomeId);
        return diaryRepository.searchByMiniHome(miniHomeId, keyword, emotion, viewerEmail, pageable)
                .map(DiaryResponse::from);
    }

    public DiaryResponse getDiary(Long diaryId, String viewerEmail) {
        Diary diary = getDiaryEntity(diaryId);

        if (!diary.isPublic() && !isOwner(diary, viewerEmail)) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "비공개 다이어리 조회 권한이 없습니다.");
        }

        return DiaryResponse.from(diary);
    }

    @Transactional
    public DiaryResponse update(Long diaryId, DiaryUpdateRequest request, String ownerEmail) {
        Diary diary = getDiaryEntity(diaryId);
        validateOwner(diary, ownerEmail, "다이어리 수정 권한이 없습니다.");

        diary.update(request.getTitle(), request.getContent(), request.getEmotion(), request.isPublicPost());
        return DiaryResponse.from(diary);
    }

    @Transactional
    public void delete(Long diaryId, String ownerEmail) {
        Diary diary = getDiaryEntity(diaryId);
        validateOwner(diary, ownerEmail, "다이어리 삭제 권한이 없습니다.");

        diaryRepository.delete(diary);
    }

    private Diary getDiaryEntity(Long diaryId) {
        return diaryRepository.findById(diaryId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "다이어리를 찾을 수 없습니다."));
    }

    private MiniHome getMiniHome(Long miniHomeId) {
        return miniHomeRepository.findById(miniHomeId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "미니홈피를 찾을 수 없습니다."));
    }

    private Member getMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }

    private void validateOwner(Diary diary, String email, String message) {
        if (!isOwner(diary, email)) {
            throw new BusinessException(HttpStatus.FORBIDDEN, message);
        }
    }

    private boolean isOwner(Diary diary, String email) {
        return diary.getMiniHome().getMember().getEmail().equals(email);
    }
}
