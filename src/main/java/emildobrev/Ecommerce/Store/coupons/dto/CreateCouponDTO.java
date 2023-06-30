package emildobrev.Ecommerce.Store.coupons.dto;

import emildobrev.Ecommerce.Store.enums.CouponsType;
import lombok.*;

import java.time.Instant;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCouponDTO {
    @NonNull
    private CouponsType code;
    @NonNull
    private double discount;
    @NonNull
    private Instant validFrom;
    @NonNull
    private Instant validTo;
}
