package kr.hs.dgsw.cyworldretro.domain.minihome;

import jakarta.validation.Valid;
import kr.hs.dgsw.cyworldretro.domain.minihome.dto.MiniHomeProfileUpdateRequest;
import kr.hs.dgsw.cyworldretro.domain.minihome.dto.MiniHomeResponse;
import kr.hs.dgsw.cyworldretro.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/minihomes")
@RequiredArgsConstructor
public class MiniHomeController {

    private final MiniHomeService miniHomeService;

    @GetMapping("/me")
    public ApiResponse<MiniHomeResponse> getMyMiniHome(Authentication authentication) {
        MiniHomeResponse response = miniHomeService.getMyMiniHome(authentication.getName());
        return ApiResponse.success("내 미니홈피 조회에 성공하였습니다.", response);
    }

    @GetMapping("/{miniHomeId}")
    public ApiResponse<MiniHomeResponse> getMiniHome(@PathVariable Long miniHomeId) {
        MiniHomeResponse response = miniHomeService.getMiniHome(miniHomeId);
        return ApiResponse.success("미니홈피 조회에 성공하였습니다.", response);
    }

    @PatchMapping("/me/profile")
    public ApiResponse<MiniHomeResponse> updateProfile(
            @Valid @RequestBody MiniHomeProfileUpdateRequest request,
            Authentication authentication
    ) {
        MiniHomeResponse response = miniHomeService.updateProfile(request, authentication.getName());
        return ApiResponse.success("미니홈피 프로필 수정에 성공하였습니다.", response);
    }

    @PostMapping("/{miniHomeId}/visit")
    public ApiResponse<MiniHomeResponse> visit(@PathVariable Long miniHomeId) {
        MiniHomeResponse response = miniHomeService.visit(miniHomeId);
        return ApiResponse.success("미니홈피 방문 처리에 성공하였습니다.", response);
    }
}
