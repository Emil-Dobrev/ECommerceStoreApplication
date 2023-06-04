package EmilDobrev.Ecommerce.Store.product.dto;

import EmilDobrev.Ecommerce.Store.enums.Category;
import EmilDobrev.Ecommerce.Store.product.Comments;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.EnumNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private String id;
    private String name;
    private String description;
    private double price;
    private Category category;
    private List<Comments> comments;
}
