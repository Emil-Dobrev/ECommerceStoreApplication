package emildobrev.ecommerce.store.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import emildobrev.ecommerce.store.enums.Category;
import emildobrev.ecommerce.store.product.Comment;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("products")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDTO implements Serializable {

    private String id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    @NonNull
    private BigDecimal price;
    @NonNull
    private Category category;
    @NonNull
    private int quantityInWarehouse;
    private List<Comment> comments;
    private Double rating;
}
