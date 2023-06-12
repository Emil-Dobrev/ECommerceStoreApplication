package EmilDobrev.Ecommerce.Store.coupons;

import EmilDobrev.Ecommerce.Store.exception.NotFoundException;
import EmilDobrev.Ecommerce.Store.product.dto.ProductDTO;
import EmilDobrev.Ecommerce.Store.user.User;
import EmilDobrev.Ecommerce.Store.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final UserRepository userRepository;
    private final CouponRepository couponRepository;

    public boolean useCoupon (String email, Coupon coupon ) {
        Coupon coup = couponRepository.findById(coupon.getId())
                .orElseThrow(() -> new NotFoundException("Coupon not found"));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(""));

        Instant now = Instant.now();

        //check user contain  coupon and if it is valid
        if(user.getCoupons().contains(coupon)
                && now.isAfter(coup.getValidFrom())
                && now.isBefore(coup.getValidTo())
         ) {
            List<Coupon> userCoupons = user.getCoupons();
            userCoupons.remove(coup);
            user.setCoupons(userCoupons);
            userRepository.save(user);
            return true;
        }
       return false;
    }

    private List<ProductDTO> reducePrice(List<ProductDTO> cart, Coupon coupon) {
     return cart
                .stream()
                .map(product-> {
                    double originalPrice = product.getPrice();
                    double discountPrice = originalPrice - (originalPrice * coupon.getDiscount() / 100);
                    product.setPrice(discountPrice);
                    return product;
                })
                .collect(Collectors.toList());

    }
}
