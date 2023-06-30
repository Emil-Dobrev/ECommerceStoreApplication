package emildobrev.Ecommerce.Store.coupons.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import emildobrev.Ecommerce.Store.enums.CouponsType;
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
    private double discount;
    @NonNull
    private Instant validFrom;
    @NonNull
    private Instant validTo;
}
