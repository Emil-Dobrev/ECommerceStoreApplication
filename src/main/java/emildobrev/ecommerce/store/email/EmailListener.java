package emildobrev.ecommerce.store.email;

import emildobrev.ecommerce.store.coupons.Coupon;
import emildobrev.ecommerce.store.coupons.CouponRepository;
import emildobrev.ecommerce.store.order.Order;
import emildobrev.ecommerce.store.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
@Slf4j
@RequiredArgsConstructor
public class EmailListener {
    private final OrderRepository orderRepository;
    private final CouponRepository couponRepository;

    @EventListener
    public void emailFailed(EmailEvent emailEvent) {
    log.info("Failed to send email");

     if(emailEvent.getValue() instanceof Order order) {
         order.setEmailSend(false);
         orderRepository.save(order);
     }
     if(emailEvent.getValue() instanceof Coupon coupon) {
         coupon.setEmailSend(false);
         couponRepository.save(coupon);
     }
 }
}
