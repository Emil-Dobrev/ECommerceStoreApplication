package emildobrev.Ecommerce.Store.coupons;

import emildobrev.Ecommerce.Store.exception.NotFoundException;
import emildobrev.Ecommerce.Store.product.dto.ProductCartDTO;
import emildobrev.Ecommerce.Store.product.dto.ProductDTO;
import emildobrev.Ecommerce.Store.user.User;
import emildobrev.Ecommerce.Store.user.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponServiceImp implements CouponService {

    private final UserRepository userRepository;
    private final CouponRepository couponRepository;

    @Transactional
    public boolean useCoupon(@NonNull String email, @NonNull Coupon coupon) {
        Coupon coup = couponRepository.findById(coupon.getId())
                .orElseThrow(() -> new NotFoundException("Coupon not found"));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(""));

        Instant now = Instant.now();

        //check user contain  coupon and if it is valid
        if (user.getCoupons().contains(coupon)
                && now.isAfter(coup.getValidFrom())
                && now.isBefore(coup.getValidTo())
        ) {

            user.setCart(reducePrice(user.getCart(), coupon));
            HashSet<Coupon> userCoupons = user.getCoupons();
            userCoupons.remove(coup);
            user.setCoupons(userCoupons);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public HashSet<ProductCartDTO> reducePrice(@NotNull HashSet<ProductCartDTO> cart, Coupon coupon) {
        return cart.stream()
                .peek(product -> {
                    BigDecimal originalPrice = product.getPrice();
                    BigDecimal discountPercentage = BigDecimal.valueOf(coupon.getDiscount());
                    BigDecimal discountAmount = originalPrice.multiply(discountPercentage)
                            .divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                    product.setPrice(originalPrice.subtract(discountAmount).setScale(2, RoundingMode.HALF_UP));
                })
                .collect(Collectors.toCollection(HashSet::new));

    }
}
