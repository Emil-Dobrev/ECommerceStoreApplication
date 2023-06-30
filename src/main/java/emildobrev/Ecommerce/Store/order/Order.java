package emildobrev.Ecommerce.Store.order;

import emildobrev.Ecommerce.Store.product.dto.ProductCartDTO;
import emildobrev.Ecommerce.Store.user.dto.UserDto;
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
public class Order {
    @Id
    private String id;
    @NonNull
    private UserDto user;
    @NonNull
    private BigDecimal totalAmount;
    @NonNull
    private Instant orderDate;
    @NonNull
    private HashSet<ProductCartDTO> products;
}
