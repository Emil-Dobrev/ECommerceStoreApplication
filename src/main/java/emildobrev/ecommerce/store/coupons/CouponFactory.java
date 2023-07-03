package emildobrev.ecommerce.store.coupons;

import emildobrev.ecommerce.store.enums.CouponsType;
import emildobrev.ecommerce.store.enums.DiscountType;

import java.time.Instant;

public class CouponFactory {


    public static Coupon createCoupon(DiscountType discountType, CouponsType type, Double discount, Instant validFrom, Instant validTo) {
        switch (discountType) {
            case PERCENTAGE -> {
                return createPercentageCoupon(type, discount, validFrom, validTo);
            }
            case FIXED_AMOUNT -> {
                return createFixedAmountCoupon(type, discount, validFrom, validTo);
            }
            default -> throw new IllegalArgumentException("Invalid coupon type: " + type);
        }
    }


    private static Coupon createPercentageCoupon(CouponsType type, Double discount, Instant validFrom, Instant validTo) {
        return Coupon.builder()
                .discountType(DiscountType.PERCENTAGE)
                .code(type)
                .discount(discount)
                .validFrom(validFrom)
                .validTo(validTo)
                .build();
    }

    private static Coupon createFixedAmountCoupon(CouponsType type, Double discount, Instant validFrom, Instant validTo) {
        return Coupon.builder()
                .discountType(DiscountType.FIXED_AMOUNT)
                .code(type)
                .discount(discount)
                .validFrom(validFrom)
                .validTo(validTo)
                .build();
    }
}

