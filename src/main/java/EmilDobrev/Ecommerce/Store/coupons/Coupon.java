package EmilDobrev.Ecommerce.Store.coupons;

import EmilDobrev.Ecommerce.Store.enums.CouponsType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document(collection = "coupons")
public class Coupon {

    @Id
    private String id;
    private CouponsType code;
    private double discount;
    private Instant validFrom;
    private Instant validTo;

}
