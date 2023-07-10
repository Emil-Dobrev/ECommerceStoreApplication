package emildobrev.ecommerce.store.util;

import emildobrev.ecommerce.store.coupons.Coupon;
import emildobrev.ecommerce.store.user.User;
import emildobrev.ecommerce.store.user.UserRepository;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
@RequiredArgsConstructor
public class Utils {

    private final UserRepository userRepository;


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
