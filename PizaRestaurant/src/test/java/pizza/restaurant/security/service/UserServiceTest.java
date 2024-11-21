package pizza.restaurant.security.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import pizza.restaurant.security.entity.Customer;
import pizza.restaurant.security.entity.Role;
import pizza.restaurant.security.entity.User;
import pizza.restaurant.security.repository.UserRepository;
import pizza.restaurant.security.request.CustomerRequest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Captor
    private ArgumentCaptor<Customer> customerCaptor;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegister() {
        // Create a mock customer request
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setUsername("testUser");
        customerRequest.setFullName("Test User");
        customerRequest.setPassword("password123");

        // Mock the behavior of the PasswordEncoder to return the encoded password
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        // Call the register method
        userService.register(customerRequest);

        // Capture the argument passed to userRepository.save
        verify(userRepository).save(customerCaptor.capture());

        // Verify the captured customer matches the expected one
        Customer capturedCustomer = customerCaptor.getValue();
        assertEquals("testUser", capturedCustomer.getUsername());
        assertEquals("Test User", capturedCustomer.getFullName());
        assertEquals(Role.Customer, capturedCustomer.getRole());
        assertEquals("encodedPassword", capturedCustomer.getPassword());
    }

    @Test
    public void testGetUserByUserName() {
        // Mock a user with a specific username
        User expectedUser = new Customer();
        expectedUser.setUsername("testUser");

        // Mock the UserRepository's findByUsername method to return the expected user
        when(userRepository.findByUsername("testUser")).thenReturn(expectedUser);

        // Call the getUserByUserName method
        User retrievedUser = userService.getUserByUserName("testUser");

        // Verify that the findByUsername method was called with the expected username
        Mockito.verify(userRepository).findByUsername("testUser");

        // Assertions
        assertEquals("testUser", retrievedUser.getUsername());
    }

    @Test
    public void testGetCustomers() {
        // Mock the behavior of baseRepository to return a list of customers
        when(userRepository.findAllByRole(Role.Customer)).thenReturn(Arrays.asList(new Customer(), new Customer()));

        // Call the getCustomers method
        List<User> customers = userService.getCustomers();

        // Assertions
        assertEquals(2, customers.size());
    }

    @Test
    public void testGetEmployees() {
        // Mock the behavior of baseRepository to return a list of employees
        when(userRepository.findAllByRole(Role.Employee)).thenReturn(Arrays.asList(new User(), new User()));

        // Call the getEmployees method
        List<User> employees = userService.getEmployees();

        // Assertions
        assertEquals(2, employees.size());
    }

}