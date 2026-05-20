package kr.hs.dgsw.cyworldretro.domain.acorn;

import jakarta.validation.Valid;
import kr.hs.dgsw.cyworldretro.domain.acorn.dto.AcornBalanceResponse;
import kr.hs.dgsw.cyworldretro.domain.acorn.dto.AcornChargeRequest;
import kr.hs.dgsw.cyworldretro.domain.acorn.dto.AcornHistoryResponse;
import kr.hs.dgsw.cyworldretro.domain.acorn.dto.AcornPurchaseRequest;
import kr.hs.dgsw.cyworldretro.domain.acorn.dto.ShopItemResponse;
import kr.hs.dgsw.cyworldretro.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/acorns")
@RequiredArgsConstructor
public class AcornController {

    private final AcornService acornService;

    @GetMapping("/balance")
    public ApiResponse<AcornBalanceResponse> getBalance(Authentication authentication) {
        AcornBalanceResponse response = acornService.getBalance(authentication.getName());
        return ApiResponse.success("도토리 잔액 조회에 성공하였습니다.", response);
    }

    @GetMapping("/shop/items")
    public ApiResponse<List<ShopItemResponse>> getShopItems() {
        List<ShopItemResponse> response = acornService.getShopItems();
        return ApiResponse.success("상점 아이템 목록 조회에 성공하였습니다.", response);
    }

    @PostMapping("/charge")
    public ApiResponse<AcornBalanceResponse> charge(
            @Valid @RequestBody AcornChargeRequest request,
            Authentication authentication
    ) {
        AcornBalanceResponse response = acornService.charge(request, authentication.getName());
        return ApiResponse.success("도토리 충전에 성공하였습니다.", response);
    }

    @PostMapping("/purchase")
    public ApiResponse<AcornBalanceResponse> purchase(
            @Valid @RequestBody AcornPurchaseRequest request,
            Authentication authentication
    ) {
        AcornBalanceResponse response = acornService.purchase(request, authentication.getName());
        return ApiResponse.success("아이템 구매에 성공하였습니다.", response);
    }

    @GetMapping("/histories")
    public ApiResponse<Page<AcornHistoryResponse>> getHistories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ) {
        Page<AcornHistoryResponse> response = acornService.getHistories(
                authentication.getName(),
                PageRequest.of(page, size)
        );
        return ApiResponse.success("도토리 거래 내역 조회에 성공하였습니다.", response);
    }
}
