package emildobrev.Ecommerce.Store.order;

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
    public ResponseEntity<Order> createOrder(Authentication authentication) {
      return ResponseEntity.ok().body(orderService.createOrder(authentication.getName()));
    }

    @PatchMapping
    public ResponseEntity<HttpStatus> cancelOrder(Authentication authentication,
                                                  @RequestParam("orderId") String orderId) {
        orderService.cancelOrder(authentication.getName(), orderId);
        return ResponseEntity.noContent().build();
    }
}
