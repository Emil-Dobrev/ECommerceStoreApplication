package emildobrev.ecommerce.store.order.dto;

import emildobrev.ecommerce.store.product.dto.ProductCartDTO;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;

@Data
@Builder
public class OrderForUserResponse {
    private int numberOfOrder;
    @NonNull
    private BigDecimal totalAmount;
    @NonNull
    private Instant orderDate;
    @NonNull
    private HashSet<ProductCartDTO> products;

}
