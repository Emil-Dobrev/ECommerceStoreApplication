package emildobrev.ecommerce.store.jobs;

import emildobrev.ecommerce.store.coupons.Coupon;
import emildobrev.ecommerce.store.coupons.CouponRepository;
import emildobrev.ecommerce.store.email.EmailService;
import emildobrev.ecommerce.store.enums.CouponsType;
import emildobrev.ecommerce.store.order.EmailMetaInformation;
import emildobrev.ecommerce.store.order.Order;
import emildobrev.ecommerce.store.order.OrderRepository;
import emildobrev.ecommerce.store.user.User;
import emildobrev.ecommerce.store.user.UserRepository;
import emildobrev.ecommerce.store.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static emildobrev.ecommerce.store.constants.Constants.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class CouponJob {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final EmailService emailService;

    @Scheduled(cron = "@monthly")
    @Transactional
    public void generateLoyalCoupons() {
        log.info("Generate loyalty coupons job started");
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        Instant oneMonthAgoInstant = oneMonthAgo.atStartOfDay(ZoneOffset.UTC).toInstant();
        var orders = orderRepository.findByIsCanceledFalse();
        //Checking how much orders we have per user
        var userIdCounts = orders.stream()
                .filter(order -> order.getOrderDate().isAfter(oneMonthAgoInstant))
                .collect(Collectors.groupingBy(Order::getUserId, Collectors.counting()));

        //Generating coupon for users which have made 2 orders for past month
        List<String> repeatingUserIds = userIdCounts.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .toList();

        repeatingUserIds.forEach(userId -> {
            var user = userRepository.findById(userId);

            if (user.isPresent()) {
                Coupon coupon = Coupon.builder()
                        .code(CouponsType.LOYALTY)
                        .discount((double) generateRandomDiscount())
                        .validFrom(Instant.now())
                        .validTo(Instant.now().plus(14, ChronoUnit.DAYS))
                        .build();
                couponRepository.save(coupon);

                var userCoupons = user.get().getCoupons();
                userCoupons.add(coupon);
                user.get().setCoupons(userCoupons);
                userRepository.save(user.get());
                emailService.sendEmail(generateEmailMetaInformation(user.get(), coupon));
            }
        });
    }

    @Scheduled(cron = "@midnight")
    public void deleteExpiredCoupons() {
        log.info("Delete expired coupons job started");
        var expiredCoupons = couponRepository.findByValidToBefore(Instant.now());
        expiredCoupons.ifPresent(couponRepository::deleteAll);
    }

    @Scheduled(cron = "0 * * * * *")
    public void generateBirthdayCoupons() {
        log.info("Generates birthday coupons job started");
        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Get the current day and month
        int currentDay = currentDate.getDayOfMonth();
        int currentMonth = currentDate.getMonth().getValue();

        var users = userRepository.findByBirthdateDayAndMonth(currentDay, currentMonth);
    }

    private int generateRandomDiscount() {
        int minDiscount = 5;
        int maxDiscount = 15;
        return ThreadLocalRandom.current().nextInt(minDiscount, maxDiscount + 1);
    }

    private EmailMetaInformation generateEmailMetaInformation(User user, Coupon coupon) {
        String fullName = Utils.getFullName(user);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        String validFrom = dateFormat.format(Date.from(coupon.getValidFrom()));
        String validTo = dateFormat.format(Date.from(coupon.getValidTo()));
        return EmailMetaInformation.builder()
                .fullName(fullName)
                .subject(SUBJECT_COUPON)
                .title(EMAIL_TITLE_COUPON)
                .header(EMAIL_HEADER_COUPON)
                .email(user.getEmail())
                .text("""
                        Dear %s,
                        We hope this email finds you in good spirits! We are delighted to inform you that as a token of our appreciation for your loyalty and continuous support, you have won a new discount coupon.
                                        
                        Coupon is valid from: %s and valid until: %s
                        Discount: %.2f%%
                        """.formatted(
                        fullName,
                        validFrom,
                        validTo,
                        coupon.getDiscount()))
                .build();
    }

}
