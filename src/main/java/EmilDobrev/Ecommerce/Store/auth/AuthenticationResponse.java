package EmilDobrev.Ecommerce.Store.auth;

import EmilDobrev.Ecommerce.Store.user.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class AuthenticationResponse {

    @JsonProperty("access_token")
    private String token;
//    @JsonProperty("user")
//    private UserDto userDto;
}
