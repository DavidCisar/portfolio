package io.dcisar.backend.user;

import io.dcisar.backend.exception.exceptions.EmailExistException;
import io.dcisar.backend.exception.exceptions.UsernameExistException;
import io.dcisar.backend.exception.exceptions.UsernameNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserDetailsService userDetailsService;
    private final LoginService loginService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) throws UsernameNotFoundException {
        loginService.authenticate(user.getUsername(), user.getPassword());
        User loggedInUser = userDetailsService.findByUsername(user.getUsername());
        UserPrincipal userPrincipal = UserPrincipal.build(loggedInUser);
        HttpHeaders jwtHeader = userDetailsService.getJWTHeader(userPrincipal);
        return new ResponseEntity<>(loggedInUser, jwtHeader, OK);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) throws EmailExistException, UsernameExistException {
        User registeredUser = userDetailsService.register(user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail());
        return new ResponseEntity<>(registeredUser, OK);
    }

    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    }


}
