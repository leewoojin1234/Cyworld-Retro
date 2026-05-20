package kr.hs.dgsw.cyworldretro.domain.acorn.dto;

import kr.hs.dgsw.cyworldretro.domain.acorn.AcornHistory;
import kr.hs.dgsw.cyworldretro.domain.acorn.AcornType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AcornHistoryResponse {

    private Long id;
    private int amount;
    private AcornType type;
    private String description;
    private LocalDateTime createdAt;

    public static AcornHistoryResponse from(AcornHistory history) {
        return AcornHistoryResponse.builder()
                .id(history.getId())
                .amount(history.getAmount())
                .type(history.getType())
                .description(history.getDescription())
                .createdAt(history.getCreatedAt())
                .build();
    }
}
