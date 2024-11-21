package pizza.restaurant.security.controller;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import pizza.restaurant.security.entity.Customer;
import pizza.restaurant.security.entity.Role;
import pizza.restaurant.security.entity.User;
import pizza.restaurant.security.request.CustomerRequest;
import pizza.restaurant.security.request.LoginRequest;
import pizza.restaurant.security.response.UserResponse;
import pizza.restaurant.security.service.CustomUserDetailsService;
import pizza.restaurant.security.service.UserService;
import pizza.restaurant.security.util.JwtUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AuthenticationController authenticationController;


    @Test
    public void testLoginSuccess() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("user1");
        loginRequest.setPassword("password123");

        UserDetails userDetails = mock(UserDetails.class);
        User user = new User(); // Assuming User is a class with username and role
        user.setUsername("user1");
        user.setRole(Role.Employee);

        // Mocking
        when(userDetailsService.loadUserByUsername("user1")).thenReturn(userDetails);
        when(userService.getUserByUserName("user1")).thenReturn(user);
        when(jwtUtil.generateToken(userDetails)).thenReturn("token");

        Claims claims = mock(Claims.class);
        when(jwtUtil.getClaimsFromToken("token")).thenReturn(claims);
        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("sub", "subject");
        when(jwtUtil.getMapFromIoJsonwebtokenClaims(claims)).thenReturn(expectedMap);
        when(jwtUtil.doGenerateRefreshToken(expectedMap, "subject")).thenReturn("refreshToken");

        // Execute
        ResponseEntity<Object> response = authenticationController.login(loginRequest);

        // Verify
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testLoginFailure() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("user1");
        loginRequest.setPassword("password123");

        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));

        ResponseEntity<Object> response = authenticationController.login(loginRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void testSaveUser() {
        CustomerRequest customerRequest = new CustomerRequest();
        customerRequest.setFullName("Renis Hila");
        customerRequest.setUsername("user1");
        customerRequest.setPassword("password123");

        Customer customerMock = new Customer();
        UserResponse userResponseMock = new UserResponse();

        when(userService.register(customerRequest)).thenReturn(customerMock);
        when(modelMapper.map(customerMock, UserResponse.class)).thenReturn(userResponseMock);

        ResponseEntity<Object> response = authenticationController.registerCustomers(customerRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testRefreshTokenWithValidToken() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        // Mocking HttpServletRequest
        when(request.getHeader("Authorization")).thenReturn("Bearer validAccessToken");

        // Mocking JwtUtil and UserDetailsService behaviors
        Claims claims = mock(Claims.class);
        when(jwtUtil.getClaimsFromToken(anyString())).thenReturn(claims);
        Map<String, Object> expectedMap = new HashMap<>();
        expectedMap.put("sub", "username"); // Ensure this value is not null
        when(jwtUtil.getMapFromIoJsonwebtokenClaims(claims)).thenReturn(expectedMap);
        when(jwtUtil.getUsernameFromToken(anyString())).thenReturn("username");
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails)).thenReturn("newToken");
        when(jwtUtil.doGenerateRefreshToken(anyMap(), anyString())).thenReturn("newRefreshToken");

        // Executing the test
        ResponseEntity<Object> response = authenticationController.refreshToken(request);

        // Verifying the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testRefreshTokenFailure() {
        String givenAccessToken = "non_existing_token";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + givenAccessToken);

        ResponseEntity<Object> response = authenticationController.refreshToken(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }


}