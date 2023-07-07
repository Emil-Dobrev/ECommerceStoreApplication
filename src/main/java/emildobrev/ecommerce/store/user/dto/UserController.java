package emildobrev.ecommerce.store.user.dto;

import emildobrev.ecommerce.store.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @PatchMapping
    public ResponseEntity<UpdateUserRequest> updateUser(@RequestBody UpdateUserRequest updateUser,
                                                        Authentication authentication) {
        return ResponseEntity.ok().body(userService.updateUser(authentication.getName(), updateUser));
    }
}
