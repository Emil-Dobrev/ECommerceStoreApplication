package emildobrev.Ecommerce.Store.auth.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationRequest {
    @NonNull
    private String email;
    @NonNull
    private String password;
}
