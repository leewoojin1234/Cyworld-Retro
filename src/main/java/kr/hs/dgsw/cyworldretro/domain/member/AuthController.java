package kr.hs.dgsw.cyworldretro.domain.member;

import jakarta.validation.Valid;
import kr.hs.dgsw.cyworldretro.domain.member.dto.MemberJoinRequest;
import kr.hs.dgsw.cyworldretro.domain.member.dto.MemberLoginRequest;
import kr.hs.dgsw.cyworldretro.domain.member.dto.ReissueTokenRequest;
import kr.hs.dgsw.cyworldretro.domain.member.dto.TokenResponse;
import kr.hs.dgsw.cyworldretro.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ApiResponse<Void> join(@Valid @RequestBody MemberJoinRequest request) {
        memberService.join(request);
        return ApiResponse.success("회원가입에 성공하였습니다.");
    }

    @PostMapping("/login")
    public ApiResponse<TokenResponse> login(@Valid @RequestBody MemberLoginRequest request) {
        TokenResponse response = memberService.login(request);
        return ApiResponse.success("로그인에 성공하였습니다.", response);
    }

    @PostMapping("/reissue")
    public ApiResponse<TokenResponse> reissue(@Valid @RequestBody ReissueTokenRequest request) {
        TokenResponse response = memberService.reissue(request);
        return ApiResponse.success("토큰 재발급에 성공하였습니다.", response);
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(Authentication authentication) {
        memberService.logout(authentication.getName());
        return ApiResponse.success("로그아웃에 성공하였습니다.");
    }
}
