package emildobrev.Ecommerce.Store.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import emildobrev.Ecommerce.Store.enums.Category;
import emildobrev.Ecommerce.Store.product.Comment;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDTO {

    private String id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    @NonNull
    private BigDecimal price;
    @NonNull
    private Category category;
    private int quantity = 1;
    private List<Comment> comments;
}
