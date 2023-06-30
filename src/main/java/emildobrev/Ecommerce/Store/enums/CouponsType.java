package emildobrev.Ecommerce.Store.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum CouponsType {
    @JsonProperty("Birthday")
    BIRTHDAY,
    @JsonProperty("Loyalty")
    LOYALTY
}
