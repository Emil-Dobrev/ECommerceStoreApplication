package emildobrev.Ecommerce.Store.coupons;

import emildobrev.Ecommerce.Store.coupons.dto.CreateCouponDTO;

public interface CouponService {
    Coupon createCoupon(CreateCouponDTO createCouponDTO);
}
