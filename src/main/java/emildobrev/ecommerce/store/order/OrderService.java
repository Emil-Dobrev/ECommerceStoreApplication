package emildobrev.ecommerce.store.order;

import emildobrev.ecommerce.store.enums.OrderStatus;
import emildobrev.ecommerce.store.order.dto.CreateOrderResponse;
import emildobrev.ecommerce.store.order.dto.OrderForUserResponse;
import lombok.NonNull;

import java.util.List;


public interface OrderService {

    CreateOrderResponse createOrder(@NonNull  String email, String couponId);

    void cancelOrder(String email, String orderId);

    void changeStatusOfOrder(String orderId, OrderStatus orderStatus);

    List<OrderForUserResponse> getAllOrdersForUser(String email);
}
