package kr.hs.dgsw.cyworldretro.domain.diary;

import jakarta.validation.Valid;
import kr.hs.dgsw.cyworldretro.domain.diary.dto.DiaryCreateRequest;
import kr.hs.dgsw.cyworldretro.domain.diary.dto.DiaryResponse;
import kr.hs.dgsw.cyworldretro.domain.diary.dto.DiaryUpdateRequest;
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
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping("/diaries")
    public ApiResponse<DiaryResponse> create(
            @Valid @RequestBody DiaryCreateRequest request,
            Authentication authentication
    ) {
        DiaryResponse response = diaryService.create(request, authentication.getName());
        return ApiResponse.success("다이어리 작성에 성공하였습니다.", response);
    }

    @GetMapping("/minihomes/{miniHomeId}/diaries")
    public ApiResponse<Page<DiaryResponse>> getDiaries(
            @PathVariable Long miniHomeId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Emotion emotion,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ) {
        Page<DiaryResponse> response = diaryService.getDiaries(
                miniHomeId,
                keyword,
                emotion,
                authentication.getName(),
                PageRequest.of(page, size)
        );
        return ApiResponse.success("다이어리 목록 조회에 성공하였습니다.", response);
    }

    @GetMapping("/diaries/{diaryId}")
    public ApiResponse<DiaryResponse> getDiary(@PathVariable Long diaryId, Authentication authentication) {
        DiaryResponse response = diaryService.getDiary(diaryId, authentication.getName());
        return ApiResponse.success("다이어리 조회에 성공하였습니다.", response);
    }

    @PatchMapping("/diaries/{diaryId}")
    public ApiResponse<DiaryResponse> update(
            @PathVariable Long diaryId,
            @Valid @RequestBody DiaryUpdateRequest request,
            Authentication authentication
    ) {
        DiaryResponse response = diaryService.update(diaryId, request, authentication.getName());
        return ApiResponse.success("다이어리 수정에 성공하였습니다.", response);
    }

    @DeleteMapping("/diaries/{diaryId}")
    public ApiResponse<Void> delete(@PathVariable Long diaryId, Authentication authentication) {
        diaryService.delete(diaryId, authentication.getName());
        return ApiResponse.success("다이어리 삭제에 성공하였습니다.");
    }
}
