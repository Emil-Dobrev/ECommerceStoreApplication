package emildobrev.ecommerce.store.coupons.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import emildobrev.ecommerce.store.enums.CouponsType;
import emildobrev.ecommerce.store.enums.DiscountType;
import lombok.*;

import java.time.Instant;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateCouponDTO {
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
}
