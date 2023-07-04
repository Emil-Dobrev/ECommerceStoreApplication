package emildobrev.ecommerce.store.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum DiscountType {
    @JsonProperty("PERCENTAGE")
    PERCENTAGE,
    @JsonProperty("FIXED_AMOUNT")
    FIXED_AMOUNT
}
