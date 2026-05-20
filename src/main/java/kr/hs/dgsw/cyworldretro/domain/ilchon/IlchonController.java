package kr.hs.dgsw.cyworldretro.domain.ilchon;

import jakarta.validation.Valid;
import kr.hs.dgsw.cyworldretro.domain.ilchon.dto.IlchonAcceptRequest;
import kr.hs.dgsw.cyworldretro.domain.ilchon.dto.IlchonRequestCreateRequest;
import kr.hs.dgsw.cyworldretro.domain.ilchon.dto.IlchonResponse;
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

import java.util.List;

@RestController
@RequestMapping("/api/ilchons")
@RequiredArgsConstructor
public class IlchonController {

    private final IlchonService ilchonService;

    @PostMapping("/requests")
    public ApiResponse<IlchonResponse> request(
            @Valid @RequestBody IlchonRequestCreateRequest request,
            Authentication authentication
    ) {
        IlchonResponse response = ilchonService.request(request, authentication.getName());
        return ApiResponse.success("일촌 신청에 성공하였습니다.", response);
    }

    @PatchMapping("/requests/{ilchonId}/accept")
    public ApiResponse<IlchonResponse> accept(
            @PathVariable Long ilchonId,
            @Valid @RequestBody IlchonAcceptRequest request,
            Authentication authentication
    ) {
        IlchonResponse response = ilchonService.accept(ilchonId, request, authentication.getName());
        return ApiResponse.success("일촌 신청 수락에 성공하였습니다.", response);
    }

    @PatchMapping("/requests/{ilchonId}/reject")
    public ApiResponse<IlchonResponse> reject(@PathVariable Long ilchonId, Authentication authentication) {
        IlchonResponse response = ilchonService.reject(ilchonId, authentication.getName());
        return ApiResponse.success("일촌 신청 거절에 성공하였습니다.", response);
    }

    @GetMapping("/requests/received")
    public ApiResponse<List<IlchonResponse>> getReceivedRequests(Authentication authentication) {
        List<IlchonResponse> response = ilchonService.getReceivedRequests(authentication.getName());
        return ApiResponse.success("받은 일촌 신청 목록 조회에 성공하였습니다.", response);
    }

    @GetMapping("/requests/sent")
    public ApiResponse<List<IlchonResponse>> getSentRequests(Authentication authentication) {
        List<IlchonResponse> response = ilchonService.getSentRequests(authentication.getName());
        return ApiResponse.success("보낸 일촌 신청 목록 조회에 성공하였습니다.", response);
    }

    @GetMapping
    public ApiResponse<List<IlchonResponse>> getAcceptedIlchons(Authentication authentication) {
        List<IlchonResponse> response = ilchonService.getAcceptedIlchons(authentication.getName());
        return ApiResponse.success("일촌 목록 조회에 성공하였습니다.", response);
    }
}
