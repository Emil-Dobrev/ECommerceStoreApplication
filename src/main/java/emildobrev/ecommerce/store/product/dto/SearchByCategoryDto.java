package emildobrev.ecommerce.store.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import emildobrev.ecommerce.store.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchByCategoryDto {
    @NonNull
    Category category;
}
