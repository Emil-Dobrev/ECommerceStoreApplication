package emildobrev.Ecommerce.Store.order;

import emildobrev.Ecommerce.Store.coupons.Coupon;
import emildobrev.Ecommerce.Store.coupons.CouponRepository;
import emildobrev.Ecommerce.Store.email.EmailService;
import emildobrev.Ecommerce.Store.exception.AccessDeniedException;
import emildobrev.Ecommerce.Store.exception.EmptyCartException;
import emildobrev.Ecommerce.Store.exception.NotFoundException;
import emildobrev.Ecommerce.Store.product.dto.ProductCartDTO;
import emildobrev.Ecommerce.Store.user.Role;
import emildobrev.Ecommerce.Store.user.User;
import emildobrev.Ecommerce.Store.user.UserRepository;
import emildobrev.Ecommerce.Store.util.Utils;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;

import static emildobrev.Ecommerce.Store.constants.Constants.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImp implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final EmailService emailService;

    @Override
    @Transactional
    public Order createOrder(@NotNull String email, String couponId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + email));

        var cart = user.getCart();
        if (cart.isEmpty()) {
            throw new EmptyCartException("Cannot create order with an empty cart.");
        }

        var totalAmount = calculateTotalAmount(user.getCart());
        LocalDateTime currentDateTime = LocalDateTime.now();
        Instant orderDate = currentDateTime.atOffset(ZoneOffset.UTC).toInstant();

        Order.OrderBuilder orderBuilder = Order.builder()
                .userId(user.getId())
                .orderDate(orderDate)
                .totalAmount(totalAmount)
                .products(cart)
                .orderNumber(OrderNumberGenerator.generateOrderNumber());
//
        if (couponId != null) {
            Coupon coupon = couponRepository.findById(couponId)
                    .orElseThrow(() -> new NotFoundException("Coupon not found with ID: " + couponId));
            //check if User contains this coupon and if now is after validFrom and now is before validTo
            if (Utils.isValidCoupon(user, coupon)) {
                double discountPercentage = coupon.getDiscount();
                BigDecimal discountAmount = totalAmount.multiply(BigDecimal.valueOf(discountPercentage / 100.0));
                totalAmount = totalAmount.subtract(discountAmount);

                orderBuilder.totalAmount(totalAmount.setScale(2, RoundingMode.UP))
                        .totalDiscount(discountAmount.setScale(2, RoundingMode.UP))
                        .couponId(couponId);
                HashSet<Coupon> coupons = user.getCoupons();
                coupons.remove(coupon);
                user.setCoupons(coupons);
                coupon.setUsed(true);
            }
        }
        user.setCart(new HashSet<>());
        userRepository.save(user);

        Order order = orderRepository.save(orderBuilder.build());
        emailService.sendEmail(generateEmailMetaInformation(user, order));
        return order;
    }

    @Override
    public void cancelOrder(String email, String orderId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + email));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with ID: " + orderId));

        if (!order.getUserId().equals(user.getId()) || user.getRoles().contains(Role.ADMIN)) {
            throw new AccessDeniedException("You don't have permissions to cancel this order");
        }
        order.setCanceled(true);
        orderRepository.save(order);
    }

    private BigDecimal calculateTotalAmount(HashSet<ProductCartDTO> productDTOS) {
        return productDTOS.stream()
                .map(ProductCartDTO::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }



    private EmailMetaInformation generateEmailMetaInformation(User user, Order order) {
        String fullName = Utils.getFullName(user);

        return EmailMetaInformation.builder()
                .fullName(fullName)
                .subject("Your Order Confirmation - Order #" + order.getOrderNumber())
                .title(EMAIL_TITLE_ORDER)
                .header(EMAIL_HEADER_ORDER)
                .email(user.getEmail())
                .text("""
                        Dear %s,
                                        
                        Thank you for choosing our services! We are pleased to inform you that your order has been successfully placed. Below are the details of your order:
                                        
                        Order Number: %s
                        Order Date: %s
                        Total Amount: %.2f
                                        
                        Please review the order details and ensure that all information is accurate. If you have any questions or need further assistance, please don't hesitate to contact our customer support team.
                                        
                        We appreciate your business and look forward to serving you.
                                        
                        Best regards,
                        """.formatted(
                        fullName,
                        order.getOrderNumber(),
                        order.getOrderDate(),
                        order.getTotalAmount()))
                .build();
    }
}
