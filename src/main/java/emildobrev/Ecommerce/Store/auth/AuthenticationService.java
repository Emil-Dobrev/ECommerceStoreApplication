package emildobrev.Ecommerce.Store.auth;

import emildobrev.Ecommerce.Store.auth.dto.AuthenticationRequest;
import emildobrev.Ecommerce.Store.auth.dto.AuthenticationResponse;
import emildobrev.Ecommerce.Store.auth.dto.RegisterRequest;
import emildobrev.Ecommerce.Store.config.JwtService;
import emildobrev.Ecommerce.Store.exception.EmailAlreadyTakenException;
import emildobrev.Ecommerce.Store.user.Role;
import emildobrev.Ecommerce.Store.user.User;
import emildobrev.Ecommerce.Store.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(List.of(Role.USER))
                .build();

       try{
           userRepository.save(user);
       } catch (DuplicateKeyException exception) {
           throw new EmailAlreadyTakenException(String.format("%s email is already taken", user.getEmail()));
       }
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                );
        authenticationManager.authenticate(authenticationToken);

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()->new  UsernameNotFoundException("User not found"));
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
