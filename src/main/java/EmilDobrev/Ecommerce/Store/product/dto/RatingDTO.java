package EmilDobrev.Ecommerce.Store.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingDTO {
    @NotBlank
    private String id;
    @NotBlank
    private Double rating;
}
