package emildobrev.ecommerce.store.coupons;

import emildobrev.ecommerce.store.coupons.dto.CreateCouponDTO;

public interface CouponService {
    Coupon createCoupon(CreateCouponDTO createCouponDTO);
}
