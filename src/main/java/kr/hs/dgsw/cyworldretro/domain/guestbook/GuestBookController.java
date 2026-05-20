package kr.hs.dgsw.cyworldretro.domain.guestbook;

import jakarta.validation.Valid;
import kr.hs.dgsw.cyworldretro.domain.guestbook.dto.GuestBookCreateRequest;
import kr.hs.dgsw.cyworldretro.domain.guestbook.dto.GuestBookResponse;
import kr.hs.dgsw.cyworldretro.domain.guestbook.dto.GuestBookUpdateRequest;
import kr.hs.dgsw.cyworldretro.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GuestBookController {

    private final GuestBookService guestBookService;

    @PostMapping("/minihomes/{miniHomeId}/guestbooks")
    public ApiResponse<GuestBookResponse> create(
            @PathVariable Long miniHomeId,
            @Valid @RequestBody GuestBookCreateRequest request,
            Authentication authentication
    ) {
        GuestBookResponse response = guestBookService.create(miniHomeId, request, authentication.getName());
        return ApiResponse.success("방명록 작성에 성공하였습니다.", response);
    }

    @GetMapping("/minihomes/{miniHomeId}/guestbooks")
    public ApiResponse<Page<GuestBookResponse>> getGuestBooks(
            @PathVariable Long miniHomeId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ) {
        Page<GuestBookResponse> response = guestBookService.getGuestBooks(
                miniHomeId,
                keyword,
                authentication.getName(),
                PageRequest.of(page, size)
        );
        return ApiResponse.success("방명록 목록 조회에 성공하였습니다.", response);
    }

    @PatchMapping("/guestbooks/{guestBookId}")
    public ApiResponse<GuestBookResponse> update(
            @PathVariable Long guestBookId,
            @Valid @RequestBody GuestBookUpdateRequest request,
            Authentication authentication
    ) {
        GuestBookResponse response = guestBookService.update(guestBookId, request, authentication.getName());
        return ApiResponse.success("방명록 수정에 성공하였습니다.", response);
    }

    @DeleteMapping("/guestbooks/{guestBookId}")
    public ApiResponse<Void> delete(@PathVariable Long guestBookId, Authentication authentication) {
        guestBookService.delete(guestBookId, authentication.getName());
        return ApiResponse.success("방명록 삭제에 성공하였습니다.");
    }
}
