package io.dcisar.backend.user;

import io.dcisar.backend.exception.exceptions.EmailExistException;
import io.dcisar.backend.exception.exceptions.EmailNotFoundException;
import io.dcisar.backend.exception.exceptions.UsernameExistException;
import io.dcisar.backend.exception.exceptions.UsernameNotFoundException;
import io.dcisar.backend.rating.RatingDTO;
import io.dcisar.backend.rating.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserDetailsService userDetailsService;
    private final LoginService loginService;
    private final RatingService ratingService;
    private final UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody User user) throws UsernameNotFoundException {
        loginService.authenticate(user.getUsername(), user.getPassword());
        User loggedInUser = userDetailsService.findByUsername(user.getUsername());
        UserPrincipal userPrincipal = UserPrincipal.build(loggedInUser);
        HttpHeaders jwtHeader = userDetailsService.getJWTHeader(userPrincipal);
        return new ResponseEntity<>(userDetailsService.mapUserToDTO(loggedInUser), jwtHeader, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO userDTO) throws EmailExistException, UsernameExistException {
        User registeredUser = userDetailsService.register(userDTO.firstName, userDTO.lastName, userDTO.username, userDTO.email);
        return new ResponseEntity<>(userDetailsService.mapUserToDTO(registeredUser), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        if (userDetailsService.deleteUser(id)) {
            return new ResponseEntity<>(
                    String.format("Deleted user with id %d from the database", id),
                    HttpStatus.OK
            );
        }
        return new ResponseEntity<>("Already submitted a rating", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/resetPassword/{email}")
    public ResponseEntity<String> resetPassword(@PathVariable String email) throws EmailNotFoundException {
        if (userDetailsService.resetPassword(email)) {
            return new ResponseEntity<>(
                    String.format("Reset password for email %s", email),
                    HttpStatus.OK
            );
        }
        return new ResponseEntity<>("Email not found!", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getUsers")
    @PreAuthorize("hasAuthority('admin:all')")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/createRating")
    public ResponseEntity<String> createRating(@RequestBody RatingDTO ratingDTO) {
        if (ratingService.createRating(ratingDTO)) {
            return new ResponseEntity<>(
                    String.format("Added rating from user %s to database", ratingDTO.userDTO.username),
                    HttpStatus.CREATED
            );
        }
        return new ResponseEntity<>("Already submitted a rating", HttpStatus.BAD_REQUEST);
    }
}
