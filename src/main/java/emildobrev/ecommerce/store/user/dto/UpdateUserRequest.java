package emildobrev.ecommerce.store.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import emildobrev.ecommerce.store.user.User;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@JsonIgnoreProperties(ignoreUnknown = true)
public record UpdateUserRequest(@NonNull String firstName,
                                @NonNull String lastName,
                                @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)String password,
                                @NonNull @DateTimeFormat Date birthdate) {
  public UpdateUserRequest(User user) {
      this(user.getFirstName(), user.getLastName(),null, user.getBirthdate());
    }

}
