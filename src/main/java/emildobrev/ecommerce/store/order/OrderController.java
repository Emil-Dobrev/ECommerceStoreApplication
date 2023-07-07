package emildobrev.ecommerce.store.order;

import emildobrev.ecommerce.store.enums.OrderStatus;
import emildobrev.ecommerce.store.order.dto.CreateOrderResponse;
import emildobrev.ecommerce.store.order.dto.OrderForUserResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    @PreAuthorize("hasAnyAuthority('USER')")
    @GetMapping
    public ResponseEntity<List<OrderForUserResponse>> getAllOrdersForUser(Authentication authentication) {
        return ResponseEntity.ok().body(orderService.getAllOrdersForUser(authentication.getName()));
    }

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(Authentication authentication,
                                                           @RequestParam(name = "couponId", required = false) String couponId
    ) {
        return ResponseEntity.ok().body(orderService.createOrder(authentication.getName(), couponId));
    }

    @PatchMapping
    public ResponseEntity<HttpStatus> cancelOrder(Authentication authentication,
                                                  @RequestParam("orderId") String orderId) {
        orderService.cancelOrder(authentication.getName(), orderId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/status")
    public ResponseEntity<HttpStatus> changeStatusOfOrder(@RequestParam("orderId") String orderId,
                                                          @RequestParam("orderStatus") OrderStatus orderStatus) {
        orderService.changeStatusOfOrder(orderId, orderStatus);
        return ResponseEntity.noContent().build();
    }
}
