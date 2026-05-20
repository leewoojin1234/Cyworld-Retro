package kr.hs.dgsw.cyworldretro.domain.ilchon.dto;

import kr.hs.dgsw.cyworldretro.domain.ilchon.Ilchon;
import kr.hs.dgsw.cyworldretro.domain.ilchon.IlchonStatus;
import kr.hs.dgsw.cyworldretro.domain.member.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class IlchonResponse {

    private Long id;
    private Long requesterId;
    private String requesterNickname;
    private Long receiverId;
    private String receiverNickname;
    private IlchonStatus status;
    private String ilchonName;
    private LocalDateTime createdAt;

    public static IlchonResponse from(Ilchon ilchon) {
        Member requester = ilchon.getRequester();
        Member receiver = ilchon.getReceiver();

        return IlchonResponse.builder()
                .id(ilchon.getId())
                .requesterId(requester.getId())
                .requesterNickname(requester.getNickname())
                .receiverId(receiver.getId())
                .receiverNickname(receiver.getNickname())
                .status(ilchon.getStatus())
                .ilchonName(ilchon.getIlchonName())
                .createdAt(ilchon.getCreatedAt())
                .build();
    }
}
