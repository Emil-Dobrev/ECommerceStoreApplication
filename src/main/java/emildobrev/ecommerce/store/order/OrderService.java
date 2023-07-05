package emildobrev.ecommerce.store.order;

import emildobrev.ecommerce.store.order.dto.CreateOrderResponse;
import lombok.NonNull;


public interface OrderService {

    CreateOrderResponse createOrder(@NonNull  String email, String couponId);

    void cancelOrder(String email, String orderId);
}
