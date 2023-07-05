package emildobrev.ecommerce.store.email;

import emildobrev.ecommerce.store.coupons.Coupon;
import emildobrev.ecommerce.store.order.Order;
import emildobrev.ecommerce.store.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
@Slf4j
@RequiredArgsConstructor
public class EmailListener {
    private final OrderRepository orderRepository;


    @EventListener
    public void emailFailed(EmailEvent emailEvent) {
    log.info("Failed to send email");
     Object object = emailEvent.getValue();
     if(object instanceof Order) {
         ((Order) object).setEmailSend(false);
         orderRepository.save((Order) object);
     }
     if(object instanceof Coupon) {
         log.error("FAIL TO SEND EMAIL");
     }

 }
}
