package pizza.restaurant.security.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pizza.restaurant.application.constants.Messages;
import pizza.restaurant.application.mapper.Response;
import pizza.restaurant.security.entity.Role;
import pizza.restaurant.security.entity.User;
import pizza.restaurant.security.request.UserRequest;
import pizza.restaurant.security.response.UserResponse;
import pizza.restaurant.security.service.UserService;

import java.lang.reflect.Type;
import java.util.List;

@RestController
@RequestMapping("users")
@Slf4j
public class UserController {

    /**
     * User Service
     */
    private final UserService userService;

    /**
     * ModelMapper instance
     */
    private final ModelMapper modelMapper;

    /**
     * PasswordEncoder instance
     */
    private final PasswordEncoder bcryptEncoder;

    /**
     * User Controller Constructor
     *
     * @param userService   UserService
     * @param modelMapper   ModelMapper
     * @param bcryptEncoder PasswordEncoder
     */
    public UserController(UserService userService, ModelMapper modelMapper, PasswordEncoder bcryptEncoder) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.bcryptEncoder = bcryptEncoder;
    }

    /**
     * List all users
     *
     * @return ResponseEntity
     */
    @GetMapping("")
    public ResponseEntity<Object> list() {
        List<User> users = this.userService.getEmployees();
        Type targetListType = new TypeToken<List<UserResponse>>() {
        }.getType();
        List<UserResponse> userResponseList = modelMapper.map(users, targetListType);

        return Response.rest(
                Messages.SUCCESS_MESSAGE,
                HttpStatus.OK,
                userResponseList,
                Messages.RECORDS_RECEIVED
        );

    }

    @GetMapping("/customers")
    public ResponseEntity<Object> listCustomers() {
        List<User> users = this.userService.getCustomers();
        Type targetListType = new TypeToken<List<UserResponse>>() {
        }.getType();
        List<UserResponse> userResponseList = modelMapper.map(users, targetListType);

        return Response.rest(
                Messages.SUCCESS_MESSAGE,
                HttpStatus.OK,
                userResponseList,
                Messages.RECORDS_RECEIVED
        );

    }

    /**
     * Create new user
     *
     * @param userRequest UserRequest
     * @return ResponseEntity
     */
    @PostMapping()
    public ResponseEntity<Object> create(@RequestBody @Valid UserRequest userRequest) {
        User user = modelMapper.map(userRequest, User.class);
        user.setPassword(bcryptEncoder.encode(userRequest.getPassword()));
        user.setRole(Role.Employee);
        user = userService.create(user);
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);

        return Response.rest(
                Messages.SUCCESS_MESSAGE,
                HttpStatus.CREATED,
                userResponse,
                Messages.RECORDS_RECEIVED
        );
    }

    /**
     * Update User
     *
     * @param id          Long
     * @param userRequest UserRequest
     * @return ResponseEntity
     */
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody @Valid UserRequest userRequest) {
        User user = userService.findById(id).orElse(null);

        if (user == null) {
            return Response.rest(Messages.FAIL_MESSAGE, HttpStatus.NOT_FOUND, null, Messages.NOT_FOUND);
        }

        user = modelMapper.map(userRequest, User.class);
        user.setPassword(bcryptEncoder.encode(userRequest.getPassword()));
        user.setRole(Role.Employee);
        user = userService.update(user, id);
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);

        return Response.rest(
                Messages.SUCCESS_MESSAGE,
                HttpStatus.ACCEPTED,
                userResponse,
                Messages.RECORDS_RECEIVED
        );
    }

    /**
     * Get single user
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable Long id) {
        User user = userService.findById(id).orElse(null);

        if (user == null) {
            return Response.rest(Messages.FAIL_MESSAGE, HttpStatus.NOT_FOUND, null, Messages.NOT_FOUND);
        }

        UserResponse userResponse = modelMapper.map(user, UserResponse.class);

        return Response.rest(
                Messages.SUCCESS_MESSAGE,
                HttpStatus.OK,
                userResponse,
                Messages.RECORDS_RECEIVED
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        User user = userService.findById(id).orElse(null);

        if (user == null) {
            return Response.rest(Messages.FAIL_MESSAGE, HttpStatus.NOT_FOUND, null, Messages.NOT_FOUND);
        }

        try {
            userService.delete(user);
            return Response.rest(Messages.SUCCESS_MESSAGE, HttpStatus.OK, null, Messages.RECORD_DELETED);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            return Response.rest(Messages.FAIL_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR, null, Messages.SERVER_ERROR);
        }
    }

}
