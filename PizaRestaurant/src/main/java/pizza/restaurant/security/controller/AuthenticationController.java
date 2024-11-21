package pizza.restaurant.security.controller;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import pizza.restaurant.application.constants.Messages;
import pizza.restaurant.application.mapper.Response;
import pizza.restaurant.security.entity.Customer;
import pizza.restaurant.security.entity.User;
import pizza.restaurant.security.request.CustomerRequest;
import pizza.restaurant.security.request.LoginRequest;
import pizza.restaurant.security.response.UserResponse;
import pizza.restaurant.security.service.CustomUserDetailsService;
import pizza.restaurant.security.service.UserService;
import pizza.restaurant.security.util.JwtUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Authentication Controller
 */
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthenticationController {

    /**
     * AuthenticationManager instance
     */
    private final AuthenticationManager authenticationManager;

    /**
     * CustomUserDetailsService instance
     */
    private final CustomUserDetailsService userDetailsService;

    /**
     * UserService instance
     */
    private final UserService userService;

    /**
     * JwtUtil instance
     */
    private final JwtUtil jwtUtil;

    private final ModelMapper modelMapper;

    /**
     * Dependency injection
     *
     * @param authenticationManager
     * @param userDetailsService
     * @param userService
     * @param jwtUtil
     * @param modelMapper
     */
    public AuthenticationController(AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService,
                                    UserService userService, JwtUtil jwtUtil, ModelMapper modelMapper) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
    }

    /**
     * This method provides the login by creating authentication token
     *
     * @param loginRequest
     * @return
     */
    @PostMapping("/tokens")
    public ResponseEntity<Object> login(@RequestBody @Valid LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (DisabledException | BadCredentialsException e) {
            log.error(e.getMessage());
            return Response.rest(Messages.FAIL_MESSAGE, HttpStatus.UNAUTHORIZED, null, Messages.BAD_CREDENTIALS);
        }

        UserDetails userdetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        User user = userService.getUserByUserName(loginRequest.getUsername());
        String token = jwtUtil.generateToken(userdetails);

        Claims claims = jwtUtil.getClaimsFromToken(token);
        Map<String, Object> expectedMap = jwtUtil.getMapFromIoJsonwebtokenClaims(claims);
        String refreshToken = jwtUtil.doGenerateRefreshToken(expectedMap, expectedMap.get("sub").toString());

        Map<String, Object> response = new HashMap<>();
        response.put("username", user.getUsername());
        response.put("role", user.getRole());
        response.put("token", token);
        response.put("refreshToken", refreshToken);

        return Response.rest(Messages.SUCCESS_MESSAGE, HttpStatus.CREATED, response, Messages.TOKEN_CREATED);
    }

    /**
     * This method is used to register new users
     *
     * @param customerRequest
     * @return
     */
    @PostMapping("/register-customers")
    public ResponseEntity<Object> registerCustomers(@RequestBody @Valid CustomerRequest customerRequest) {
        Customer customer = userService.register(customerRequest);
        UserResponse userResponse = modelMapper.map(customer, UserResponse.class);
        return Response.rest(Messages.SUCCESS_MESSAGE, HttpStatus.CREATED, userResponse, Messages.USER_CREATED);
    }

    /**
     * This method is used to refresh the expired token
     *
     * @param request --header 'isRefreshToken: true', --header 'Authorization: Bearer // insert bearer token'
     * @return
     */
    @Transactional
    @PutMapping("/tokens/refresh")
    public ResponseEntity<Object> refreshToken(HttpServletRequest request) {
        //takes token from header
        String givenAccessToken = request.getHeader("Authorization").substring(7);

        // From the HttpRequest get the claims
        Claims claims = jwtUtil.getClaimsFromToken(givenAccessToken);

        if(claims == null){
            return Response.rest(Messages.BAD_CREDENTIALS, HttpStatus.UNAUTHORIZED, null, Messages.BAD_CREDENTIALS);
        }

        Map<String, Object> expectedMap = jwtUtil.getMapFromIoJsonwebtokenClaims(claims);

        UserDetails userdetails = userDetailsService.loadUserByUsername(jwtUtil.getUsernameFromToken(givenAccessToken));
        String token = jwtUtil.generateToken(userdetails);
        String refreshToken = jwtUtil.doGenerateRefreshToken(expectedMap, expectedMap.get("sub").toString());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("refreshToken", refreshToken);

        return Response.rest(Messages.SUCCESS_MESSAGE, HttpStatus.OK, response, Messages.REFRESH_TOKEN_CREATED);
    }

}
