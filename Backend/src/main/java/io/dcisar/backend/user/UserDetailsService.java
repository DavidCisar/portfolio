package io.dcisar.backend.user;

import io.dcisar.backend.config.jwt.JWTTokenProvider;
import io.dcisar.backend.exception.exceptions.EmailExistException;
import io.dcisar.backend.exception.exceptions.EmailNotFoundException;
import io.dcisar.backend.exception.exceptions.UsernameExistException;
import io.dcisar.backend.rating.Rating;
import io.dcisar.backend.rating.RatingRepository;
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

@Service
@Transactional
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

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