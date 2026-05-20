package kr.hs.dgsw.cyworldretro.domain.acorn;

import kr.hs.dgsw.cyworldretro.domain.acorn.dto.AcornBalanceResponse;
import kr.hs.dgsw.cyworldretro.domain.acorn.dto.AcornChargeRequest;
import kr.hs.dgsw.cyworldretro.domain.acorn.dto.AcornHistoryResponse;
import kr.hs.dgsw.cyworldretro.domain.acorn.dto.AcornPurchaseRequest;
import kr.hs.dgsw.cyworldretro.domain.acorn.dto.ShopItemResponse;
import kr.hs.dgsw.cyworldretro.domain.member.Member;
import kr.hs.dgsw.cyworldretro.domain.member.MemberRepository;
import kr.hs.dgsw.cyworldretro.domain.minihome.MiniHome;
import kr.hs.dgsw.cyworldretro.domain.minihome.MiniHomeRepository;
import kr.hs.dgsw.cyworldretro.domain.minihome.SkinType;
import kr.hs.dgsw.cyworldretro.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AcornService {

    private final MemberRepository memberRepository;
    private final MiniHomeRepository miniHomeRepository;
    private final AcornHistoryRepository acornHistoryRepository;

    public AcornBalanceResponse getBalance(String email) {
        Member member = getMember(email);
        return AcornBalanceResponse.builder()
                .acorn(member.getAcorn())
                .build();
    }

    public List<ShopItemResponse> getShopItems() {
        return Arrays.stream(ShopItemType.values())
                .map(ShopItemResponse::from)
                .toList();
    }

    @Transactional
    public AcornBalanceResponse charge(AcornChargeRequest request, String email) {
        Member member = getMember(email);
        member.addAcorn(request.getAmount());

        saveHistory(member, request.getAmount(), AcornType.CHARGE, "도토리 충전");
        return AcornBalanceResponse.builder()
                .acorn(member.getAcorn())
                .build();
    }

    @Transactional
    public AcornBalanceResponse purchase(AcornPurchaseRequest request, String email) {
        Member member = getMember(email);
        MiniHome miniHome = miniHomeRepository.findByMember(member)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "미니홈피를 찾을 수 없습니다."));
        ShopItemType itemType = request.getItemType();

        member.useAcorn(itemType.getPrice());
        applyItem(miniHome, itemType);
        saveHistory(member, -itemType.getPrice(), AcornType.PURCHASE, itemType.getDisplayName() + " 구매");

        return AcornBalanceResponse.builder()
                .acorn(member.getAcorn())
                .build();
    }

    public Page<AcornHistoryResponse> getHistories(String email, Pageable pageable) {
        Member member = getMember(email);
        return acornHistoryRepository.findAllByMember(member, pageable)
                .map(AcornHistoryResponse::from);
    }

    private void applyItem(MiniHome miniHome, ShopItemType itemType) {
        if (itemType.isSkin()) {
            miniHome.updateSkin(SkinType.valueOf(itemType.getValue()));
            return;
        }

        if (itemType.isBgm()) {
            miniHome.updateBgm(itemType.getValue());
        }
    }

    private void saveHistory(Member member, int amount, AcornType type, String description) {
        AcornHistory history = AcornHistory.builder()
                .member(member)
                .amount(amount)
                .type(type)
                .description(description)
                .build();
        acornHistoryRepository.save(history);
    }

    private Member getMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }
}
