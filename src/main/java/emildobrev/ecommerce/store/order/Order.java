package emildobrev.ecommerce.store.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import emildobrev.ecommerce.store.enums.OrderStatus;
import emildobrev.ecommerce.store.product.dto.ProductCartDTO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;

@Document(collection = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {
    @Id
    @JsonIgnore
    private String id;
    @NonNull
    private String userId;
    @NonNull
    private BigDecimal totalAmount;
    @NonNull
    private Instant orderDate;
    @NonNull
    private HashSet<ProductCartDTO> products;
    private String couponId;
    private BigDecimal totalDiscount;
    private String orderNumber;
    private OrderStatus orderStatus;
    @JsonIgnore
    private boolean isEmailSend = true;
}
