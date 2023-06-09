package emildobrev.ecommerce.store.integration.jobs;

import emildobrev.ecommerce.store.coupons.Coupon;
import emildobrev.ecommerce.store.coupons.CouponRepository;
import emildobrev.ecommerce.store.email.EmailService;
import emildobrev.ecommerce.store.enums.CouponsType;
import emildobrev.ecommerce.store.jobs.CouponJob;
import emildobrev.ecommerce.store.order.OrderRepository;
import emildobrev.ecommerce.store.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CouponJobsTest {
    @Mock
    private  OrderRepository orderRepository;
    @Mock
    private  UserRepository userRepository;
    @Mock
    private  CouponRepository couponRepository;
    @Mock
    private EmailService emailService;
    @InjectMocks
    private CouponJob couponJob;

    @BeforeEach
    public void setUp() {
        couponJob = new CouponJob(orderRepository, userRepository, couponRepository, emailService);
    }

    @Test
    void testDeleteExpiredCoupons() {
        // Mocking the expired coupons
        Coupon coupon1 = Coupon.builder()
                .id("12345")
                .code(CouponsType.LOYALTY)
                .discount(10.0)
                .validFrom(Instant.parse("2023-07-01T00:00:00Z"))
                .validTo(Instant.parse("2023-07-15T23:59:59Z"))
                .build();
        Coupon coupon2 = Coupon.builder()
                .id("12345")
                .code(CouponsType.LOYALTY)
                .discount(10.0)
                .validFrom(Instant.parse("2023-07-01T00:00:00Z"))
                .validTo(Instant.parse("2023-07-15T23:59:59Z"))
                .build();

        List<Coupon> expiredCoupons = Arrays.asList(coupon1, coupon2);

        // Mocking the behavior of the coupon repository
        Mockito.when(couponRepository.findByValidToBefore(Mockito.any(Instant.class)))
                .thenReturn(Optional.of(expiredCoupons));

        // Calling the method
        couponJob.deleteExpiredCoupons();

        // Verifying that the findByValidToBefore method was called on the coupon repository
        Mockito.verify(couponRepository, Mockito.times(1)).findByValidToBefore(Mockito.any(Instant.class));

        // Verifying that the deleteAll method was called on the coupon repository
        Mockito.verify(couponRepository, Mockito.times(1)).deleteAll(Mockito.eq(expiredCoupons));
    }
}
