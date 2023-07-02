package emildobrev.ecommerce.store.product.dto;

import emildobrev.ecommerce.store.enums.Category;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCartDTO {
    private String id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    @NonNull
    private BigDecimal price;
    @NonNull
    private Category category;
    @Builder.Default
    private int orderQuantity = 1;
}
