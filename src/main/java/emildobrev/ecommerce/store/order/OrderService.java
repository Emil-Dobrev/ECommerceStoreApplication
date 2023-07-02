package emildobrev.ecommerce.store.order;

import lombok.NonNull;


public interface OrderService {

    Order createOrder(@NonNull  String email, String couponId);

    void cancelOrder(String email, String orderId);
}
