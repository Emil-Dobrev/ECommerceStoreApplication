package EmilDobrev.Ecommerce.Store.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collation = "users")
@AllArgsConstructor
@Data
public class User {

    private String email;
    private String password;

}
