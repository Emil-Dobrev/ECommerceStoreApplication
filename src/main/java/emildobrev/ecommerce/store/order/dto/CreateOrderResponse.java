package emildobrev.ecommerce.store.order.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import emildobrev.ecommerce.store.product.dto.ProductCartDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;

@AllArgsConstructor
@Data
@Builder
public class CreateOrderResponse {

    @NonNull
    private String userFullName;
    @NonNull
    private BigDecimal totalAmount;
    @NonNull
    private Instant orderDate;
    @NonNull
    private HashSet<ProductCartDTO> products;
    private String couponId;
    private BigDecimal totalDiscount;
    @NonNull
    private String orderNumber;

}
