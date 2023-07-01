package emildobrev.Ecommerce.Store.jobs;

import emildobrev.Ecommerce.Store.coupons.Coupon;
import emildobrev.Ecommerce.Store.coupons.CouponRepository;
import emildobrev.Ecommerce.Store.enums.CouponsType;
import emildobrev.Ecommerce.Store.order.Order;
import emildobrev.Ecommerce.Store.order.OrderRepository;
import emildobrev.Ecommerce.Store.user.User;
import emildobrev.Ecommerce.Store.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class CouponJob {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private  final CouponRepository couponRepository;

    @Scheduled(cron = "@monthly")
    public void generateLoyalCoupons() {
        log.info("Generate loyalty coupons job started");
        Instant oneMonthAgo = Instant.now().minus(1, ChronoUnit.MONTHS);
        var orders = orderRepository.findByCanceledFalse();
        //Checking how much orders we have per user
        var userIdCounts = orders.stream()
                .filter(order -> order.getOrderDate().isAfter(oneMonthAgo))
                .collect(Collectors.groupingBy(Order::getUserId, Collectors.counting()));

        //Generating coupon for users which have made 2 orders for past month
        List<String> repeatingUserIds = userIdCounts.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .toList();

        repeatingUserIds.forEach(userId -> {
            Optional<User> user = userRepository.findById(userId);

            if (user.isPresent()) {
                Coupon coupon = Coupon.builder()
                        .code(CouponsType.LOYALTY)
                        .discount(generateRandomDiscount())
                        .validFrom(Instant.now())
                        .validTo(Instant.now().plus(14, ChronoUnit.DAYS))
                        .build();
                couponRepository.save(coupon);

                var userCoupons = user.get().getCoupons();
                userCoupons.add(coupon);
                user.get().setCoupons(userCoupons);
                userRepository.save(user.get());
            }
        });
    }

    @Scheduled(cron = "@midnight")
    public void deleteExpiredCoupons() {
        log.info("Delete expired coupons job started");
        var expiredCoupons = couponRepository.findByValidToBefore(Instant.now());
        expiredCoupons.ifPresent(couponRepository::deleteAll);
    }

    private int generateRandomDiscount() {
        int minDiscount = 5;
        int maxDiscount = 15;
        return ThreadLocalRandom.current().nextInt(minDiscount, maxDiscount + 1);
    }

}
