package kr.hs.dgsw.cyworldretro.domain.member.dto;

import kr.hs.dgsw.cyworldretro.domain.member.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberInfoResponse {

    private Long id;
    private String email;
    private String nickname;
    private int acorn;

    public static MemberInfoResponse from(Member member) {
        return MemberInfoResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .acorn(member.getAcorn())
                .build();
    }
}
