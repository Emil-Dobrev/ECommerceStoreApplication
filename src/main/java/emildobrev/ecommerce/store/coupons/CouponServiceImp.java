package emildobrev.ecommerce.store.coupons;

import emildobrev.ecommerce.store.coupons.dto.CreateCouponDTO;
import emildobrev.ecommerce.store.product.dto.ProductCartDTO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponServiceImp implements CouponService {

    private final CouponRepository couponRepository;
    private final ModelMapper modelMapper;

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

    @Override
    public Coupon createCoupon(@NonNull CreateCouponDTO createCouponDTO) {
        Coupon coupon = modelMapper.map(createCouponDTO, Coupon.class);
       return couponRepository.save(coupon);

    }
}