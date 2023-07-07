package emildobrev.ecommerce.store.user.dto;

import emildobrev.ecommerce.store.coupons.Coupon;
import emildobrev.ecommerce.store.product.dto.ProductCartDTO;
import emildobrev.ecommerce.store.user.Role;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private String id;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    private String email;
    @NonNull
    private String password;
    private HashSet<ProductCartDTO> cart;
    @NonNull
    private List<Role> roles;
    @NonNull
    @DateTimeFormat
    private Date birthdate;
    private HashSet<Coupon> coupons;
}
