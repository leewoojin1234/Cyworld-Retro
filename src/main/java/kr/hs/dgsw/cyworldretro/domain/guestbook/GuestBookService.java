package kr.hs.dgsw.cyworldretro.domain.guestbook;

import kr.hs.dgsw.cyworldretro.domain.guestbook.dto.GuestBookCreateRequest;
import kr.hs.dgsw.cyworldretro.domain.guestbook.dto.GuestBookResponse;
import kr.hs.dgsw.cyworldretro.domain.guestbook.dto.GuestBookUpdateRequest;
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
public class GuestBookService {

    private final GuestBookRepository guestBookRepository;
    private final MiniHomeRepository miniHomeRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public GuestBookResponse create(Long miniHomeId, GuestBookCreateRequest request, String writerEmail) {
        MiniHome miniHome = getMiniHome(miniHomeId);
        Member writer = getMember(writerEmail);

        GuestBook guestBook = GuestBook.builder()
                .miniHome(miniHome)
                .writer(writer)
                .content(request.getContent())
                .isSecret(request.isSecret())
                .build();

        return GuestBookResponse.from(guestBookRepository.save(guestBook));
    }

    public Page<GuestBookResponse> getGuestBooks(Long miniHomeId, String keyword, String viewerEmail, Pageable pageable) {
        getMiniHome(miniHomeId);
        return guestBookRepository.searchByMiniHome(miniHomeId, keyword, viewerEmail, pageable)
                .map(GuestBookResponse::from);
    }

    @Transactional
    public GuestBookResponse update(Long guestBookId, GuestBookUpdateRequest request, String writerEmail) {
        GuestBook guestBook = getGuestBook(guestBookId);

        if (!isWriter(guestBook, writerEmail)) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "방명록 수정 권한이 없습니다.");
        }

        guestBook.update(request.getContent(), request.isSecret());
        return GuestBookResponse.from(guestBook);
    }

    @Transactional
    public void delete(Long guestBookId, String requesterEmail) {
        GuestBook guestBook = getGuestBook(guestBookId);

        if (!isWriter(guestBook, requesterEmail) && !isMiniHomeOwner(guestBook, requesterEmail)) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "방명록 삭제 권한이 없습니다.");
        }

        guestBookRepository.delete(guestBook);
    }

    private GuestBook getGuestBook(Long guestBookId) {
        return guestBookRepository.findById(guestBookId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "방명록을 찾을 수 없습니다."));
    }

    private MiniHome getMiniHome(Long miniHomeId) {
        return miniHomeRepository.findById(miniHomeId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "미니홈피를 찾을 수 없습니다."));
    }

    private Member getMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }

    private boolean isWriter(GuestBook guestBook, String email) {
        return guestBook.getWriter().getEmail().equals(email);
    }

    private boolean isMiniHomeOwner(GuestBook guestBook, String email) {
        return guestBook.getMiniHome().getMember().getEmail().equals(email);
    }
}
