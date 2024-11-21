package pizza.restaurant.security.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pizza.restaurant.security.entity.Role;
import pizza.restaurant.security.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    public void testLoadUserByUsername_UserFound() {
        // Setup
        String username = "testUser";
        pizza.restaurant.security.entity.User mockUser = new pizza.restaurant.security.entity.User();
        mockUser.setUsername(username);
        mockUser.setPassword("password");
        mockUser.setRole(Role.Customer); // Assuming Role is an enum or a class

        when(userRepository.findByUsername(username)).thenReturn(mockUser);

        // Execute
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        // Verify
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("Customer")));
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        // Setup
        String username = "nonExistingUser";
        when(userRepository.findByUsername(username)).thenReturn(null);

        // Execute & Verify
        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername(username);
        });
    }


}
