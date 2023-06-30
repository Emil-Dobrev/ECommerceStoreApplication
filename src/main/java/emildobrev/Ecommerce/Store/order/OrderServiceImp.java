package emildobrev.Ecommerce.Store.order;

import emildobrev.Ecommerce.Store.exception.NotFoundException;
import emildobrev.Ecommerce.Store.order.dto.CreateOrderDTO;
import emildobrev.Ecommerce.Store.product.dto.ProductDTO;
import emildobrev.Ecommerce.Store.user.User;
import emildobrev.Ecommerce.Store.user.UserRepository;
import emildobrev.Ecommerce.Store.user.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class OrderServiceImp implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public Order createOrder(@NotNull String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + email));

        var totalAmount = calculateTotalAmount(user.getCart());

        CreateOrderDTO createOrderDTO = CreateOrderDTO.create(user.getId(), totalAmount);
        Order order = Order.builder()
                .user(user)
                .totalAmount(createOrderDTO.totalAmount())
                .orderDate(createOrderDTO.orderDate())
                .cart(user.getCart())
                .build();

        return orderRepository.save(order);
    }

    private BigDecimal calculateTotalAmount(HashSet<ProductDTO> productDTOS) {
        return productDTOS.stream()
                .map(ProductDTO::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
