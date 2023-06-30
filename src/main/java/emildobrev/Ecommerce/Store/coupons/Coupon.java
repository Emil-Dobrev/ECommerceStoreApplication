package emildobrev.Ecommerce.Store.coupons;

import emildobrev.Ecommerce.Store.enums.CouponsType;
import lombok.*;
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
    @NonNull
    private CouponsType code;
    @NonNull
    private double discount;
    @NonNull
    private Instant validFrom;
    @NonNull
    private Instant validTo;
    private boolean isUsed = false;

}
