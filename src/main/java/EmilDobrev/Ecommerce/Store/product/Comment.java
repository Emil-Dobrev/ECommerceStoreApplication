package EmilDobrev.Ecommerce.Store.product;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @NotBlank
    @Transient
    @JsonAlias("id")
    String productId;
    //TODO take this property by JWT token when implemented
    private String createdBy;
    @NotBlank
    private String description;
}
