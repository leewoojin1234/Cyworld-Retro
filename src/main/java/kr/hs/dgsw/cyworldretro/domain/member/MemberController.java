package kr.hs.dgsw.cyworldretro.domain.member;

import kr.hs.dgsw.cyworldretro.domain.member.dto.MemberInfoResponse;
import kr.hs.dgsw.cyworldretro.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me")
    public ApiResponse<MemberInfoResponse> getMyInfo(Authentication authentication) {
        MemberInfoResponse response = memberService.getMyInfo(authentication.getName());
        return ApiResponse.success("내 정보 조회에 성공하였습니다.", response);
    }
}
