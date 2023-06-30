package emildobrev.Ecommerce.Store.order;

import emildobrev.Ecommerce.Store.coupons.Coupon;
import emildobrev.Ecommerce.Store.coupons.CouponRepository;
import emildobrev.Ecommerce.Store.exception.AccessDeniedException;
import emildobrev.Ecommerce.Store.exception.EmptyCartException;
import emildobrev.Ecommerce.Store.exception.NotFoundException;
import emildobrev.Ecommerce.Store.product.dto.ProductCartDTO;
import emildobrev.Ecommerce.Store.user.Role;
import emildobrev.Ecommerce.Store.user.User;
import emildobrev.Ecommerce.Store.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class OrderServiceImp implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final ModelMapper modelMapper;

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

        if(couponId != null) {
            Coupon coupon = couponRepository.findById(couponId)
                    .orElseThrow(() -> new NotFoundException("Coupon not found with ID: " + couponId));
            double discountPercentage = coupon.getDiscount();
            BigDecimal discountAmount = totalAmount.multiply(BigDecimal.valueOf(discountPercentage / 100.0));
            totalAmount = totalAmount.subtract(discountAmount);
        }

        Order order = Order.builder()
                .userId(user.getId())
                .totalAmount(totalAmount)
                .orderDate(orderDate)
                .products(user.getCart())
                .build();

        user.setCart(new HashSet<>());
        userRepository.save(user);
        return orderRepository.save(order);
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
}
