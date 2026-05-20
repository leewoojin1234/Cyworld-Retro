package kr.hs.dgsw.cyworldretro.domain.guestbook.dto;

import kr.hs.dgsw.cyworldretro.domain.guestbook.GuestBook;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GuestBookResponse {

    private Long id;
    private Long miniHomeId;
    private Long writerId;
    private String writerNickname;
    private String content;
    private boolean secret;
    private LocalDateTime createdAt;

    public static GuestBookResponse from(GuestBook guestBook) {
        return GuestBookResponse.builder()
                .id(guestBook.getId())
                .miniHomeId(guestBook.getMiniHome().getId())
                .writerId(guestBook.getWriter().getId())
                .writerNickname(guestBook.getWriter().getNickname())
                .content(guestBook.getContent())
                .secret(guestBook.isSecret())
                .createdAt(guestBook.getCreatedAt())
                .build();
    }
}
