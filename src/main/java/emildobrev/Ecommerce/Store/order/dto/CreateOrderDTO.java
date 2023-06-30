package emildobrev.Ecommerce.Store.order.dto;

import lombok.NonNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public record CreateOrderDTO(
        @NonNull String userId,
        @NonNull BigDecimal totalAmount,
        Instant orderDate
) {
    public static CreateOrderDTO create(@NonNull String userId, @NonNull BigDecimal totalAmount) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        Instant orderDate = currentDateTime.atOffset(ZoneOffset.UTC).toInstant();
        return new CreateOrderDTO(userId, totalAmount, orderDate);
    }
}
