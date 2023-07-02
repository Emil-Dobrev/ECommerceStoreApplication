package emildobrev.ecommerce.store.user;

import emildobrev.ecommerce.store.coupons.Coupon;
import emildobrev.ecommerce.store.product.dto.ProductCartDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Document(collection = "users")
@AllArgsConstructor
@Builder
@Data
public class User implements UserDetails {
    @Id
    private String id;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    @Indexed(unique = true)
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .toList();

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
