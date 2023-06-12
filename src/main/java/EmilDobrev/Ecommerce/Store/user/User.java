package EmilDobrev.Ecommerce.Store.user;


import EmilDobrev.Ecommerce.Store.coupons.Coupon;
import EmilDobrev.Ecommerce.Store.product.dto.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Document(collection = "users")
@AllArgsConstructor
@Builder
@Data
public class User implements UserDetails {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    @NotNull
    @Indexed(unique = true)
    private String email;
    private String password;
    private List<ProductDTO> cart;
    private Role role;
    private Instant birthdate;
    private List<Coupon> coupons;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
