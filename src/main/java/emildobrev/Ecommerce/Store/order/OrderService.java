package emildobrev.Ecommerce.Store.order;

import lombok.NonNull;

import java.math.BigDecimal;

public interface OrderService {

    Order createOrder(@NonNull  String userId, @NonNull BigDecimal totalAmount);
}
