package emildobrev.ecommerce.store.order;

import emildobrev.ecommerce.store.coupons.Coupon;
import emildobrev.ecommerce.store.coupons.CouponRepository;
import emildobrev.ecommerce.store.email.EmailMetaInformation;
import emildobrev.ecommerce.store.email.EmailService;
import emildobrev.ecommerce.store.enums.DiscountType;
import emildobrev.ecommerce.store.enums.OrderStatus;
import emildobrev.ecommerce.store.exception.AccessDeniedException;
import emildobrev.ecommerce.store.exception.EmptyCartException;
import emildobrev.ecommerce.store.exception.NotFoundException;
import emildobrev.ecommerce.store.order.dto.CreateOrderResponse;
import emildobrev.ecommerce.store.product.dto.ProductCartDTO;
import emildobrev.ecommerce.store.user.Role;
import emildobrev.ecommerce.store.user.User;
import emildobrev.ecommerce.store.user.UserRepository;
import emildobrev.ecommerce.store.util.Utils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;

import static emildobrev.ecommerce.store.constants.Constants.*;

@Service
@RequiredArgsConstructor

public class OrderServiceImp implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final EmailService emailService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public CreateOrderResponse createOrder(@NotNull String email, String couponId) {
        var user = userRepository.findByEmail(email)
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
                .orderStatus(OrderStatus.PENDING)
                .orderNumber(OrderNumberGenerator.generateOrderNumber());

        if (couponId != null) {
            Coupon coupon = couponRepository.findById(couponId)
                    .orElseThrow(() -> new NotFoundException("Coupon not found with ID: " + couponId));

            useCouponDiscount(couponId, user, totalAmount, orderBuilder, coupon);
        }
        user.setCart(new HashSet<>());
        userRepository.save(modelMapper.map(user, User.class));
        Order order = orderRepository.save(orderBuilder.build());
        emailService.sendEmail(generateEmailMetaInformation(user, order), order);

        return CreateOrderResponse.builder()
                .totalDiscount(order.getTotalDiscount())
                .userFullName(Utils.getFullName(user))
                .orderNumber(order.getOrderNumber())
                .totalAmount(order.getTotalAmount())
                .orderDate(order.getOrderDate())
                .couponId(couponId)
                .products(cart)
                .build();
    }

    @Override
    public void cancelOrder(String email, String orderId) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + email));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with ID: " + orderId));

        if (!order.getUserId().equals(user.getId()) || user.getRoles().contains(Role.ADMIN)) {
            throw new AccessDeniedException("You don't have permissions to cancel this order");
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
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
                                  
                        """.formatted(
                        fullName,
                        order.getOrderNumber(),
                        order.getOrderDate(),
                        order.getTotalAmount()))
                .build();
    }

    private void useCouponDiscount(String couponId, User user, BigDecimal totalAmount, Order.OrderBuilder orderBuilder, Coupon coupon) {
        //check if User contains this coupon and if now is after validFrom and now is before validTo
        if (Utils.isValidCoupon(user, coupon)) {
            BigDecimal discountAmount = getDiscountAmount(
                    coupon.getDiscountType(),
                    totalAmount,
                    BigDecimal.valueOf(coupon.getDiscount()));
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

    private BigDecimal getDiscountAmount(DiscountType discountType, BigDecimal originalPrice, BigDecimal discount) {
        switch (discountType) {
            case PERCENTAGE -> {
                return originalPrice.multiply(discount)
                        .divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
            }
            case FIXED_AMOUNT -> {
                return originalPrice.subtract(discount);
            }
            default -> throw new IllegalArgumentException("Invalid coupon type: " + discountType);
        }
    }
}
