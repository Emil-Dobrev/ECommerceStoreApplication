package emildobrev.ecommerce.store.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum CouponsType {
    @JsonProperty("Birthday")
    BIRTHDAY,
    @JsonProperty("Loyalty")
    LOYALTY
}
