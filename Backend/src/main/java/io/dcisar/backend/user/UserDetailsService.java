package io.dcisar.backend.user;

import io.dcisar.backend.config.jwt.JWTTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static io.dcisar.backend.config.SecurityConstant.JWT_TOKEN_HEADER;
import static io.dcisar.backend.user.Role.ROLE_ADMIN;

@Service
@Transactional
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Value("${ADMIN.USERNAME}")
    private String admin_username;
    @Value("${ADMIN.FIRSTNAME}")
    private String admin_firstname;
    @Value("${ADMIN.LASTNAME}")
    private String admin_lastname;
    @Value("${ADMIN.USERNAME}")
    private String admin_email;
    @Value("${ADMIN.PASSWORD}")
    private String admin_password;

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JWTTokenProvider jwtTokenProvider;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User not found by username " + username);
        } else {
            return UserPrincipal.build(user.get());
        }
    }

    private String encodePassword(String password) {
        String encodedPassword = passwordEncoder.encode(password);
        return encodedPassword;
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

    @PostConstruct
    private void createAdmin() {
        Optional<User> user = userRepository.findByUsername(this.admin_username);
        if (!user.isPresent()) {
            User admin = User.builder()
                    .username(this.admin_username)
                    .firstName(this.admin_firstname)
                    .lastName(this.admin_lastname)
                    .email(this.admin_email)
                    .password(encodePassword(this.admin_password))
                    .role(ROLE_ADMIN.name())
                    .authorities(ROLE_ADMIN.getAuthorities())
                    .build();
            userRepository.save(admin);
        }
    }

    public UserDTO mapUserToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}