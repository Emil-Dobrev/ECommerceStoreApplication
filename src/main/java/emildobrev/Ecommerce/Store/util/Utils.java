package emildobrev.Ecommerce.Store.util;

import emildobrev.Ecommerce.Store.coupons.Coupon;
import emildobrev.Ecommerce.Store.user.User;

import java.time.Instant;

public class Utils {

    public static String getFullName(User user) {
        return  String.format("%s %s", user.getFirstName(), user.getLastName()).trim();
    }

    public static boolean isValidCoupon(User user, Coupon coupon) {
        Instant now = Instant.now();
        return user.getCoupons().contains(coupon) &&
                coupon.getValidFrom().isBefore(now) &&
                coupon.getValidTo().isAfter(now);
    }
}
