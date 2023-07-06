package emildobrev.ecommerce.store.order;

import emildobrev.ecommerce.store.enums.OrderStatus;
import emildobrev.ecommerce.store.order.dto.OrderForUserResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


import java.util.List;


@Repository
public interface OrderRepository extends MongoRepository<Order, String > {
    List<Order> findByOrderStatusNotIn(List<OrderStatus> excludedStatuses);
    List<Order> findAllByUserId(String userId);

}
