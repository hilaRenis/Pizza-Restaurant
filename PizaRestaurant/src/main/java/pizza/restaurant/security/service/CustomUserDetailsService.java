package pizza.restaurant.security.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pizza.restaurant.security.repository.UserRepository;

import java.util.Arrays;
import java.util.List;

/**
 * CustomUserDetails Service
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * UserRepository instance
     */
    private final UserRepository userRepository;

    /**
     * Dependency injection
     *
     * @param userRepository
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * This method gets a user by username
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<SimpleGrantedAuthority> roles;

        pizza.restaurant.security.entity.User user = userRepository.findByUsername(username);
        if (user != null) {
            roles = Arrays.asList(new SimpleGrantedAuthority(user.getRole().toString()));
            return new User(user.getUsername(), user.getPassword(), roles);
        }
        throw new UsernameNotFoundException("User not found with the name " + username);
    }

}

