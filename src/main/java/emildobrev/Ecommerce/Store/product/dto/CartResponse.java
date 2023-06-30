package emildobrev.Ecommerce.Store.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CartResponse {
        private List<ProductDTO> cart;
        private String message;
}
