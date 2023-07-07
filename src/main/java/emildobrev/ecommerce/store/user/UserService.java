package emildobrev.ecommerce.store.user;

import emildobrev.ecommerce.store.user.dto.UpdateUserRequest;

public interface UserService {


    UpdateUserRequest updateUser(String email, UpdateUserRequest updateUser);
}
