package emildobrev.Ecommerce.Store.coupons;

import emildobrev.Ecommerce.Store.coupons.dto.CreateCouponDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;

    @PostMapping
    public ResponseEntity<Coupon> createCoupon(@RequestBody CreateCouponDTO createCouponDTO) {
     return ResponseEntity.ok().body(couponService.createCoupon(createCouponDTO));
    }
}
