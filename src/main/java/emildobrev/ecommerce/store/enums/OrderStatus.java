package emildobrev.ecommerce.store.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum OrderStatus {
    @JsonProperty("Pending")
    PENDING,
    @JsonProperty("Processing")
    PROCESSING,
    @JsonProperty("Shipped")
    SHIPPED,
    @JsonProperty("In Transit")
    IN_TRANSIT,
    @JsonProperty("Out for Delivery")
    OUT_FOR_DELIVERY,
    @JsonProperty("Delivered")
    DELIVERED,
    @JsonProperty("Cancelled")
    CANCELLED,
    @JsonProperty("Returned")
    RETURNED,
    @JsonProperty("Refunded")
    REFUNDED,
    @JsonProperty("On Hold")
    ON_HOLD
}
