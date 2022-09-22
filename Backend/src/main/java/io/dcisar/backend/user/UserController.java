package io.dcisar.backend.user;

import io.dcisar.backend.exception.exceptions.UsernameNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserDetailsService userDetailsService;
    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody User user) throws UsernameNotFoundException {
        loginService.authenticate(user.getUsername(), user.getPassword());
        User loggedInUser = userDetailsService.findByUsername(user.getUsername());
        UserPrincipal userPrincipal = UserPrincipal.build(loggedInUser);
        HttpHeaders jwtHeader = userDetailsService.getJWTHeader(userPrincipal);
        return new ResponseEntity<>(userDetailsService.mapUserToDTO(loggedInUser), jwtHeader, HttpStatus.OK);
    }
}
