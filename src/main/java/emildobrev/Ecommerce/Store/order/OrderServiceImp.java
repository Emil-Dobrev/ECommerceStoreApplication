package emildobrev.Ecommerce.Store.order;

import emildobrev.Ecommerce.Store.exception.NotFoundException;
import emildobrev.Ecommerce.Store.order.dto.CreateOrderDTO;
import emildobrev.Ecommerce.Store.user.User;
import emildobrev.Ecommerce.Store.user.UserRepository;
import emildobrev.Ecommerce.Store.user.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderServiceImp implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public Order createOrder(@NotNull String userId, @NotNull BigDecimal totalAmount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));

        CreateOrderDTO createOrderDTO = CreateOrderDTO.create(userId, totalAmount);
        Order order = Order.builder()
                .user(user)
                .totalAmount(createOrderDTO.totalAmount())
                .orderDate(createOrderDTO.orderDate())
                .cart(user.getCart())
                .build();

        return orderRepository.save(order);

    }

}
