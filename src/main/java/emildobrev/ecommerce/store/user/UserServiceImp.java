package emildobrev.ecommerce.store.user;

import emildobrev.ecommerce.store.user.dto.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static emildobrev.ecommerce.store.constants.Constants.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public UpdateUserRequest updateUser(String email, UpdateUserRequest updateUser) {
        var existingUser = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
        if (!existingUser.getFirstName().equals(updateUser.firstName())) {
            existingUser.setFirstName(updateUser.firstName());
        }
        if (!existingUser.getLastName().equals(updateUser.lastName())) {
            existingUser.setLastName(updateUser.lastName());
        }
        if (updateUser.password() != null && !passwordEncoder.encode(updateUser.password()).matches(existingUser.getPassword())) {
            existingUser.setPassword(passwordEncoder.encode(updateUser.password()));
        }
        if (!existingUser.getBirthdate().equals(updateUser.birthdate())) {
            existingUser.setBirthdate(updateUser.birthdate());
        }
        userRepository.save(existingUser);
     return new UpdateUserRequest(existingUser);
    }
}
