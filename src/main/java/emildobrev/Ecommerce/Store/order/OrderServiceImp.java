package emildobrev.Ecommerce.Store.order;

import emildobrev.Ecommerce.Store.exception.NotFoundException;
import emildobrev.Ecommerce.Store.order.dto.CreateOrderDTO;
import emildobrev.Ecommerce.Store.product.dto.ProductCartDTO;
import emildobrev.Ecommerce.Store.user.User;
import emildobrev.Ecommerce.Store.user.UserRepository;
import emildobrev.Ecommerce.Store.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class OrderServiceImp implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public Order createOrder(@NotNull String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + email));

        var totalAmount = calculateTotalAmount(user.getCart());

        CreateOrderDTO createOrderDTO = CreateOrderDTO.create(user.getId(), totalAmount);
        Order order = Order.builder()
                .user(modelMapper.map(user, UserDto.class))
                .totalAmount(createOrderDTO.totalAmount())
                .orderDate(createOrderDTO.orderDate())
                .products(user.getCart())
                .build();

        user.setCart(new HashSet<>());
        userRepository.save(user);
        return orderRepository.save(order);
    }

    private BigDecimal calculateTotalAmount(HashSet<ProductCartDTO> productDTOS) {
        return productDTOS.stream()
                .map(ProductCartDTO::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
