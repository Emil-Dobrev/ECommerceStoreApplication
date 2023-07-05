package emildobrev.ecommerce.store.order;

import emildobrev.ecommerce.store.order.dto.CreateOrderResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
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
}
