package emildobrev.ecommerce.store.coupons;

import emildobrev.ecommerce.store.enums.CouponsType;
import emildobrev.ecommerce.store.enums.DiscountType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
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
    private Double discount;
    @NonNull
    private Instant validFrom;
    @NonNull
    private Instant validTo;
    @NonNull
    private DiscountType discountType;
    private boolean isUsed = false;
}
