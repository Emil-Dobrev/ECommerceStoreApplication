package emildobrev.Ecommerce.Store.order;

import lombok.NonNull;


public interface OrderService {

    Order createOrder(@NonNull  String email);

    void cancelOrder(String email, String orderId);
}
