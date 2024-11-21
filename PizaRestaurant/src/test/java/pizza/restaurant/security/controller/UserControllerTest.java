package pizza.restaurant.security.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import pizza.restaurant.security.entity.Role;
import pizza.restaurant.security.entity.User;
import pizza.restaurant.security.request.UserRequest;
import pizza.restaurant.security.response.UserResponse;
import pizza.restaurant.security.service.UserService;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Mock
    private PasswordEncoder bcryptEncoder;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDelete() {
        // Mocking the userService.findById() and userService.delete() methods
        User user = new User();
        user.setId(1L);
        when(userService.findById(1L)).thenReturn(Optional.of(user));

        // Call the controller method
        ResponseEntity<Object> responseEntity = userController.delete(1L);

        // Verify the response and status code
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testList() {
        // Arrange
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        List<User> mockUsers = Arrays.asList(user1, user2);

        UserResponse response1 = new UserResponse();
        response1.setId(1L);

        UserResponse response2 = new UserResponse();
        response2.setId(2L);

        List<UserResponse> mockResponses = Arrays.asList(response1, response2);

        when(userService.list()).thenReturn(mockUsers);

        Type targetListType = new TypeToken<List<UserResponse>>() {
        }.getType();
        when(modelMapper.map(mockUsers, targetListType)).thenReturn(mockResponses);

        // Act
        ResponseEntity<Object> response = userController.list();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }


    @Test
    public void testListCustomers() {
        // Setup
        List<User> customers = Arrays.asList(new User(), new User());
        when(userService.getCustomers()).thenReturn(customers);

        List<UserResponse> customerResponses = Arrays.asList(new UserResponse(), new UserResponse());
        when(modelMapper.map(customers, new TypeToken<List<UserResponse>>() {}.getType()))
                .thenReturn(customerResponses);

        // Execute
        ResponseEntity<Object> response = userController.listCustomers();

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetUserFound() {
        // Setup
        Long id = 1L;
        Optional<User> user = Optional.of(new User());
        UserResponse userResponse = new UserResponse();

        when(userService.findById(id)).thenReturn(user);
        when(modelMapper.map(user.get(), UserResponse.class)).thenReturn(userResponse);

        // Execute
        ResponseEntity<Object> response = userController.get(id);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Further assertions to check the response body
    }

    @Test
    public void testGetUserNotFound() {
        // Setup
        Long id = 1L;
        when(userService.findById(id)).thenReturn(Optional.empty());

        // Execute
        ResponseEntity<Object> response = userController.get(id);

        // Verify
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }




    @Test
    public void testCreateUser() {
        // Arrange
        UserRequest mockRequest = new UserRequest();
        mockRequest.setUsername("user1");

        User mappedUser = new User();
        mappedUser.setUsername("user1");
        mappedUser.setRole(Role.Customer);

        User storedUser = new User();
        storedUser.setId(1L);
        storedUser.setUsername("user1");
        storedUser.setRole(Role.Customer);
        UserResponse mockUserResponse = new UserResponse();
        mockUserResponse.setId(1L);
        mockUserResponse.setUsername("user1");
        mockUserResponse.setRole(Role.Customer);

        when(modelMapper.map(mockRequest, User.class)).thenReturn(mappedUser);
        when(userService.create(any(User.class))).thenReturn(storedUser);
        when(modelMapper.map(storedUser, UserResponse.class)).thenReturn(mockUserResponse);

        // Act
        ResponseEntity<Object> response = userController.create(mockRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testUpdateUserWhenUserNotFound() {
        Long id = 1L;
        UserRequest mockRequest = new UserRequest();

        when(userService.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = userController.update(id, mockRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateUserSuccessfully() {
        Long id = 1L;
        UserRequest mockRequest = new UserRequest();
        User mockUser = new User();
        User updatedUser = new User();
        UserResponse mockResponse = new UserResponse();

        when(userService.findById(id)).thenReturn(Optional.of(mockUser));
        when(modelMapper.map(mockRequest, User.class)).thenReturn(mockUser);
        when(userService.update(mockUser, id)).thenReturn(updatedUser);
        when(modelMapper.map(updatedUser, UserResponse.class)).thenReturn(mockResponse);

        ResponseEntity<Object> response = userController.update(id, mockRequest);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

    @Test
    public void testDeleteUserWhenUserNotFound() {
        Long id = 1L;

        when(userService.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = userController.delete(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteUserSuccessfully() {
        Long id = 1L;
        User mockUser = new User();

        when(userService.findById(id)).thenReturn(Optional.of(mockUser));

        ResponseEntity<Object> response = userController.delete(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteUserWithException() {
        Long id = 1L;
        User mockUser = new User();

        when(userService.findById(id)).thenReturn(Optional.of(mockUser));
        doThrow(new RuntimeException("Some error")).when(userService).delete(mockUser);

        ResponseEntity<Object> response = userController.delete(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}