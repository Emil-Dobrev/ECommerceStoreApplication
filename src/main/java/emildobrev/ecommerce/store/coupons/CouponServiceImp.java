package emildobrev.ecommerce.store.coupons;

import emildobrev.ecommerce.store.coupons.dto.CreateCouponDTO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponServiceImp implements CouponService {

    private final CouponRepository couponRepository;
    private final ModelMapper modelMapper;

    @Override
    public Coupon saveCouponToDb(@NonNull CreateCouponDTO createCouponDTO) {
        Coupon coupon = modelMapper.map(createCouponDTO, Coupon.class);
        return couponRepository.save(coupon);
    }

}
