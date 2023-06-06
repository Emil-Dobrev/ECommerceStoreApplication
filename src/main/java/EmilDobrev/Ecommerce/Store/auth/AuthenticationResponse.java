package EmilDobrev.Ecommerce.Store.auth;

import EmilDobrev.Ecommerce.Store.user.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class AuthenticationResponse {

    private String token;
    private UserDto userDto;
}
