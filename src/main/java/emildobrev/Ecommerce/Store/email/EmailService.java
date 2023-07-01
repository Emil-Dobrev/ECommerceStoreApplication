package emildobrev.Ecommerce.Store.email;

import emildobrev.Ecommerce.Store.coupons.Coupon;
import emildobrev.Ecommerce.Store.order.EmailMetaInformation;
import emildobrev.Ecommerce.Store.order.Order;

public interface EmailService {

        void sendEmailForCoupon(EmailMetaInformation emailMetaInformation);
}
