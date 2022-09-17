package io.dcisar.backend.user;

import io.dcisar.backend.config.jwt.JWTTokenProvider;
import io.dcisar.backend.exception.exceptions.EmailExistException;
import io.dcisar.backend.exception.exceptions.UsernameExistException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static io.dcisar.backend.config.SecurityConstant.JWT_TOKEN_HEADER;
import static io.dcisar.backend.user.Role.ROLE_USER;

@Service
@Transactional
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JWTTokenProvider jwtTokenProvider;
    private final LoginAttemptService loginAttemptService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User not found by username " + username);
        } else {
            validateLoginAttempt(user.get());
            return UserPrincipal.build(user.get());
        }
    }

    private void validateLoginAttempt(User user) {
        if (user.isNotLocked() && loginAttemptService.hasExceededMaxAttempts(user.getUsername())) {
            user.setNotLocked(false);
        }
    }

    public User register(String firstName, String lastName, String username, String email)
            throws EmailExistException, UsernameExistException
    {
        validateNewUsernameAndEmail(username, email);
        String password = generatePassword();
        System.out.println("password = " + password);
        String encodedPassword = encodePassword(password);
        User userToBeRegistered = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .email(email)
                .password(encodedPassword)
                .isNotLocked(true)
                .role(ROLE_USER.name())
                .authorities(ROLE_USER.getAuthorities())
                .build();
        userRepository.save(userToBeRegistered);
        return userToBeRegistered;
    }

    private String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    private String encodePassword(String password) {
        String encodedPassword = passwordEncoder.encode(password);
        return encodedPassword;
    }

    private void validateNewUsernameAndEmail(
            String newUsername,
            String newEmail) throws UsernameExistException, EmailExistException {
        if (userRepository.findByUsername(newUsername).isPresent()) {
            throw new UsernameExistException("Username already exists!");
        }
        if (userRepository.findByEmail(newEmail).isPresent()) {
            throw new EmailExistException("Email already exists!");
        }
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User findByUsername(String username) throws io.dcisar.backend.exception.exceptions.UsernameNotFoundException {
        if (userRepository.findByUsername(username).isPresent()) {
            return userRepository.findByUsername(username).get();
        }
        throw new io.dcisar.backend.exception.exceptions.UsernameNotFoundException("Username not found: " + username);
    }

    public HttpHeaders getJWTHeader(UserPrincipal userPrincipal) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJWTToken(userPrincipal));
        return headers;
    }

}