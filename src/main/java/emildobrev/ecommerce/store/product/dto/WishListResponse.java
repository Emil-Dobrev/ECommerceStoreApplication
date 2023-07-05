package emildobrev.ecommerce.store.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@AllArgsConstructor
@Data
@Builder
public class WishListResponse {
    @NonNull
    private String productName;
    @NonNull
    private String message;
}
